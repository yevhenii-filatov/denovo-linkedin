package com.dataox.linkedinscraper.service;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.NotScrapedLinkedinProfile;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import com.dataox.linkedinscraper.utils.NotificationUtils;
import com.dataox.linkedinscraper.utils.ObjectUtils;
import com.dataox.notificationservice.service.TelegramNotificationsServiceProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dataox.linkedinscraper.service.error.detector.LinkedinError.*;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@Slf4j
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
    private final ObjectMapper objectMapper;

    public ScrapingResultsDTO scrape(List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape) {
        ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions);
        WebDriver webDriver = launcher.getWebDriver();
        try {
            loginService.performLogin(webDriver);
        } catch (Exception e) {
            LinkedinError linkedinError = errorDetector.detect(webDriver);
            launcher.close();
            if (linkedinError.equals(NO_ERRORS))
                return createScrapedLinkedinProfilesDTO(linkedinProfilesToScrape, e);
            return createScrapedLinkedinProfilesDTO(linkedinProfilesToScrape, linkedinError);
        }
        List<LinkedinProfile> scrapedLinkedinProfiles = new ArrayList<>();
        List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles = new ArrayList<>();
        for (LinkedinProfileToScrapeDTO linkedinProfile : linkedinProfilesToScrape) {
            try {
                CollectedProfileSourcesDTO source = scraper.scrape(webDriver, linkedinProfile);
                scrapedLinkedinProfiles.add(parser.parse(source));
            } catch (Exception e) {
                ScrapingResultsDTO scrapingResultsDTO = resolveException(linkedinProfilesToScrape,
                        webDriver,
                        scrapedLinkedinProfiles,
                        notScrapedLinkedinProfiles,
                        linkedinProfile,
                        e);
                if (scrapingResultsDTO != null) {
                    launcher.close();
                    return scrapingResultsDTO;
                }
            }
        }
        launcher.close();
        return new ScrapingResultsDTO(scrapedLinkedinProfiles, notScrapedLinkedinProfiles);
    }

    private ScrapingResultsDTO resolveException(List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape,
                                                WebDriver webDriver,
                                                List<LinkedinProfile> scrapedLinkedinProfiles,
                                                List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles,
                                                LinkedinProfileToScrapeDTO linkedinProfile,
                                                Exception e) {
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (isScraperShouldStop(linkedinError))
            return createScrapedLinkedinProfilesDTO(scrapedLinkedinProfiles, linkedinProfilesToScrape, linkedinError);
        if (isProfileReusable(linkedinError)) {
            notScrapedLinkedinProfiles.add(createNotScrapedProfile(linkedinError, linkedinProfile.getProfileURL(), false));
            return null;
        }
        if (linkedinError.equals(NO_ERRORS))
            notScrapedLinkedinProfiles.add(createNotScrapedProfile(e, linkedinProfile.getProfileURL(), true));
        else
            notScrapedLinkedinProfiles.add(createNotScrapedProfile(linkedinError, linkedinProfile.getProfileURL(), true));
        return null;
    }

    private <T> NotScrapedLinkedinProfile createNotScrapedProfile(T cause, String profileURL, boolean notReusable) {
        NotScrapedLinkedinProfile notScrapedLinkedinProfile = new NotScrapedLinkedinProfile();
        notScrapedLinkedinProfile.setProfileURL(profileURL);
        notScrapedLinkedinProfile.setNotReusable(notReusable);
        setErrorDescription(cause, profileURL, notScrapedLinkedinProfile);
        return notScrapedLinkedinProfile;
    }

    private ScrapingResultsDTO createScrapedLinkedinProfilesDTO(List<LinkedinProfile> scrapedLinkedinProfiles,
                                                                List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape,
                                                                LinkedinError linkedinError) {
        ScrapingResultsDTO scrapingResultsDTO = new ScrapingResultsDTO();
        scrapingResultsDTO.setSuccessfulProfiles(scrapedLinkedinProfiles);
        List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles = new ArrayList<>();
        for (LinkedinProfileToScrapeDTO linkedinProfileToScrapeDTO : linkedinProfilesToScrape) {
            NotScrapedLinkedinProfile notScrapedLinkedinProfile = new NotScrapedLinkedinProfile();
            notScrapedLinkedinProfile.setProfileURL(linkedinProfileToScrapeDTO.getProfileURL());
            setErrorDescription(linkedinError, linkedinProfileToScrapeDTO.getProfileURL(), notScrapedLinkedinProfile);
            notScrapedLinkedinProfile.setNotReusable(false);
        }
        scrapingResultsDTO.setNotScrapedLinkedinProfiles(notScrapedLinkedinProfiles);
        return scrapingResultsDTO;
    }

    private <T> ScrapingResultsDTO createScrapedLinkedinProfilesDTO(List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape, T cause) {
        ScrapingResultsDTO scrapingResults = new ScrapingResultsDTO();
        List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles = new ArrayList<>();
        for (LinkedinProfileToScrapeDTO linkedinProfileToScrapeDTO : linkedinProfilesToScrape) {
            NotScrapedLinkedinProfile notScrapedLinkedinProfile = new NotScrapedLinkedinProfile();
            notScrapedLinkedinProfile.setProfileURL(linkedinProfileToScrapeDTO.getProfileURL());
            notScrapedLinkedinProfile.setNotReusable(false);
            setErrorDescription(cause, linkedinProfileToScrapeDTO.getProfileURL(), notScrapedLinkedinProfile);
            notScrapedLinkedinProfiles.add(notScrapedLinkedinProfile);
        }
        scrapingResults.setNotScrapedLinkedinProfiles(notScrapedLinkedinProfiles);
        return scrapingResults;
    }

    private <T> void setErrorDescription(T cause, String profileURL, NotScrapedLinkedinProfile notScrapedLinkedinProfile) {
        try {
            notScrapedLinkedinProfile.setErrorDescription(objectMapper.writeValueAsString(cause));
        } catch (JsonProcessingException e) {
            log.error("Failed to write cause as string", e);
            notificationsService.send(NotificationUtils.createExceptionMessage(e, profileURL, applicationContext.getId()));
            notScrapedLinkedinProfile.setErrorDescription("Failed to write cause as string using ObjectMapper");
        }
    }

    private boolean isScraperShouldStop(LinkedinError linkedinError) {
        return ObjectUtils.equalsAny(linkedinError, LOGGED_OUT, ISNT_QUITE_RIGHT, BANNED, RESTRICTED);
    }

    private boolean isProfileReusable(LinkedinError linkedinError) {
        return ObjectUtils.equalsAny(linkedinError, UNKNOWN_ERROR, OOPS_ITS_NOT_YOU_ITS_US, SOMETHING_WENT_WRONG);
    }

}
