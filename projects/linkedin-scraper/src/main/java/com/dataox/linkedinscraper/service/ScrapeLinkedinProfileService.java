package com.dataox.linkedinscraper.service;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.NotScrapedLinkedinProfile;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinException;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinParsingException;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinScrapingException;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import com.dataox.linkedinscraper.utils.NotificationUtils;
import com.dataox.notificationservice.service.TelegramNotificationsServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.dataox.linkedinscraper.service.error.detector.LinkedinError.*;
import static com.dataox.linkedinscraper.utils.ObjectUtils.equalsAny;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public ScrapingResultsDTO scrape(List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape) {
        List<LinkedinProfile> successfullyScraped = new ArrayList<>();
        List<NotScrapedLinkedinProfile> notScraped = new ArrayList<>();
        LinkedinProfileToScrapeDTO currentProfileToScrape = null;
        try (ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions)) {
            WebDriver webDriver = launcher.getWebDriver();
            loginService.performLogin(webDriver);
            for (LinkedinProfileToScrapeDTO linkedinProfileToScrapeDTO : linkedinProfilesToScrape) {
                currentProfileToScrape = linkedinProfileToScrapeDTO;
                LinkedinProfile linkedinProfile = scrapeSingleProfile(currentProfileToScrape, webDriver, notScraped);
                if (nonNull(linkedinProfile))
                    successfullyScraped.add(linkedinProfile);
            }
        } catch (LinkedinException e) {
            addTheRestOfProfilesToNotScrapedList(currentProfileToScrape, linkedinProfilesToScrape, notScraped);
            rabbitListenerEndpointRegistry.stop();
            notificationsService.send(NotificationUtils.createScraperStoppedMessage(e, applicationContext.getId()));
        }
        return new ScrapingResultsDTO(successfullyScraped, notScraped);
    }

    private void addTheRestOfProfilesToNotScrapedList(LinkedinProfileToScrapeDTO currentProfileToScrape,
                                                      List<LinkedinProfileToScrapeDTO> linkedinProfilesToScrape,
                                                      List<NotScrapedLinkedinProfile> notScraped) {
        if (isNull(currentProfileToScrape)) {
            linkedinProfilesToScrape.stream()
                    .map(LinkedinProfileToScrapeDTO::getProfileURL)
                    .map(ScrapeLinkedinProfileService::createReusableNotScrapedProfile)
                    .forEach(notScraped::add);
        } else {
            int failedProfileIndex = linkedinProfilesToScrape.indexOf(currentProfileToScrape);
            if (failedProfileIndex + 1 == linkedinProfilesToScrape.size())
                return;
            IntStream.range(failedProfileIndex + 1, linkedinProfilesToScrape.size())
                    .mapToObj(linkedinProfilesToScrape::get)
                    .map(LinkedinProfileToScrapeDTO::getProfileURL)
                    .map(ScrapeLinkedinProfileService::createReusableNotScrapedProfile)
                    .forEach(notScraped::add);
        }
    }

    private static NotScrapedLinkedinProfile createReusableNotScrapedProfile(String profileURL) {
        return new NotScrapedLinkedinProfile(profileURL, StringUtils.EMPTY, true);
    }

    private LinkedinProfile scrapeSingleProfile(LinkedinProfileToScrapeDTO profile, WebDriver webDriver, List<NotScrapedLinkedinProfile> notScraped) {
        try {
            CollectedProfileSourcesDTO sources = scraper.scrape(webDriver, profile);
            return parser.parse(sources);
        } catch (LinkedinScrapingException e) {
            LinkedinError linkedinError = errorDetector.detect(webDriver);
            boolean isProfileReusable = isProfileReusable(linkedinError);
            boolean isScraperShouldStop = isScraperShouldStop(linkedinError);
            e.setLinkedinError(linkedinError);
            handleParsingAndScrapingException(profile.getProfileURL(), e, isProfileReusable, notScraped);
            if (isScraperShouldStop)
                throw e;
            return null;
        } catch (LinkedinParsingException e) {
            handleParsingAndScrapingException(profile.getProfileURL(), e, false, notScraped);
            throw e;
        }
    }

    private void handleParsingAndScrapingException(String profileUrl,
                                                   LinkedinException e,
                                                   boolean isProfileReusable,
                                                   List<NotScrapedLinkedinProfile> notScraped) {
        NotScrapedLinkedinProfile notScrapedProfile = new NotScrapedLinkedinProfile(profileUrl, e.asString(), isProfileReusable);
        notScraped.add(notScrapedProfile);
        notificationsService.send(NotificationUtils.createExceptionMessage(e, profileUrl, applicationContext.getId()));
    }

    private boolean isScraperShouldStop(LinkedinError linkedinError) {
        return equalsAny(linkedinError, ISNT_QUITE_RIGHT, LOGGED_OUT);
    }

    private boolean isProfileReusable(LinkedinError linkedinError) {
        return !equalsAny(linkedinError, PROFILE_IS_NOT_AVAILABLE, PAGE_NOT_FOUND, DONT_HAVE_ACCESS_TO_PROFILE);
    }

}
