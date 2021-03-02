package com.dataox.linkedinscraper.service;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfilesDTO;
import com.dataox.linkedinscraper.exceptions.ParserException;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import com.dataox.linkedinscraper.utils.NotificationUtils;
import com.dataox.linkedinscraper.utils.ObjectUtils;
import com.dataox.notificationservice.service.TelegramNotificationsServiceProvider;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.linkedinscraper.service.error.detector.LinkedinError.*;
import static com.dataox.linkedinscraper.utils.NotificationUtils.*;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@Service
@RequiredArgsConstructor
public class ScrapeLinkedinProfileService {
    private final LoginService loginService;
    private final LinkedinProfileScraper scraper;
    private final LinkedinProfileParser parser;
    private final ChromeOptions chromeOptions;
    private final LinkedinErrorDetector errorDetector;
    private final TelegramNotificationsServiceProvider notificationsService;
    private final ApplicationContext applicationContext;

    public LinkedinProfilesDTO scrape(List<String> profileUrls) {
        ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions);
        WebDriver webDriver = launcher.getWebDriver();
        loginService.performLogin(webDriver);
        List<CollectedProfileSourcesDTO> sourcesDTOS = new ArrayList<>();
        List<String> unavailableProfileUrls = new ArrayList<>();
        for (String profileUrl : profileUrls) {
            try {
                sourcesDTOS.add(scraper.scrape(webDriver, profileUrl));
            } catch (Exception e) {
                LinkedinError linkedinError = errorDetector.detect(webDriver);
                if (linkedinError.equals(NO_ERRORS)) {
                    notificationsService.send(NotificationUtils.createExceptionMessage(e, profileUrl, applicationContext.getId()));
                    throw e;
                }
                if (!isExceptionResolved(webDriver, sourcesDTOS, unavailableProfileUrls, profileUrl)) {
                    launcher.close();
                    return createLinkedinProfilesDTO(profileUrls, sourcesDTOS, unavailableProfileUrls);
                }
            }
        }
        launcher.close();
        return createLinkedinProfilesDTO(profileUrls, sourcesDTOS, unavailableProfileUrls);
    }

    private LinkedinProfilesDTO createLinkedinProfilesDTO(List<String> profileUrls,
                                                          List<CollectedProfileSourcesDTO> sourcesDTOS,
                                                          List<String> unavailableProfileUrls) {
        LinkedinProfilesDTO linkedinProfilesDTO = new LinkedinProfilesDTO();
        List<LinkedinProfile> parsedProfiles = parseProfiles(sourcesDTOS);
        linkedinProfilesDTO.setSuccessfulProfiles(parsedProfiles);
        linkedinProfilesDTO.setUnavailableProfileUrls(unavailableProfileUrls);
        linkedinProfilesDTO.setFailedToScrapeProfileUrls(resolveFailedProfiles(profileUrls, sourcesDTOS));
        return linkedinProfilesDTO;
    }

    private List<String> resolveFailedProfiles(List<String> profileUrls, List<CollectedProfileSourcesDTO> sourcesDTOS) {
        List<String> collectedProfileUrls = sourcesDTOS.stream()
                .map(CollectedProfileSourcesDTO::getProfileUrl)
                .collect(Collectors.toList());
        return profileUrls.stream()
                .filter(s -> !collectedProfileUrls.contains(s))
                .collect(Collectors.toList());
    }

    private List<LinkedinProfile> parseProfiles(List<CollectedProfileSourcesDTO> sourcesDTOS) {
        List<LinkedinProfile> parsedProfiles = new ArrayList<>();
        for (CollectedProfileSourcesDTO sourceDTO : sourcesDTOS) {
            try {
                parsedProfiles.add(parser.parse(sourceDTO));
            } catch (Exception e) {
                notificationsService.send(createExceptionMessage(e, sourceDTO.getProfileUrl(), applicationContext.getId()));
                throw ParserException.exceptionOccurred(e.getClass().getSimpleName(), e.getMessage(), sourceDTO.getProfileUrl());
            }
        }
        return parsedProfiles;
    }

    private boolean isExceptionResolved(WebDriver webDriver,
                                        List<CollectedProfileSourcesDTO> sourcesDTOS,
                                        List<String> unavailableProfileUrls,
                                        String profileUrl) {
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (linkedinError.equals(NO_ERRORS)) {
            return true;
        }
        if (linkedinError.equals(LOGGED_OUT)) {
            notificationsService.send(createLinkedinErrorMessage(linkedinError, applicationContext.getId()));
            return false;
        }
        if (linkedinError.equals(LinkedinError.ISNT_QUITE_RIGHT)) {
            notificationsService.send(createLinkedinErrorMessage(linkedinError, applicationContext.getId()));
            return false;
        }
        if (ObjectUtils.equalsAny(linkedinError, DONT_HAVE_ACCESS_TO_PROFILE, PROFILE_IS_NOT_AVAILABLE, PAGE_NOT_FOUND)) {
            unavailableProfileUrls.add(profileUrl);
            return true;
        }
        if (ObjectUtils.equalsAny(linkedinError, SOMETHING_WENT_WRONG, OOPS_ITS_NOT_YOU_ITS_US)) {
            return isProfileSuccessfullyReScraped(webDriver, sourcesDTOS, profileUrl);
        }
        return false;
    }

    private boolean isProfileSuccessfullyReScraped(WebDriver webDriver, List<CollectedProfileSourcesDTO> sourcesDTOS, String profileUrl) {
        try {
            sourcesDTOS.add(scraper.scrape(webDriver, profileUrl));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
