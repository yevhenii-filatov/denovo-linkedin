package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinScrapingException;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.scraping.configuration.property.QueryProperties;
import com.dataox.linkedinscraper.scraping.scrapers.subscrapers.*;
import com.dataox.notificationservice.service.NotificationsService;
import com.dataox.okhttputils.OkHttpTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LinkedinProfileScraper {

    AboutSectionScraper aboutSectionScraper;
    ActivitiesScraper activitiesScraper;
    EducationScraper educationScraper;
    ExperienceScraper experienceScraper;
    HeaderSectionScraper headerSectionScraper;
    LicenseScraper licenseScraper;
    RecommendationsScraper recommendationsScraper;
    AllSkillsScraper allSkillsScraper;
    SkillsWithEndorsementsScraper skillsWithEndorsementsScraper;
    VolunteersScraper volunteersScraper;
    InterestsScraper interestsScraper;
    AccomplishmentsScraper accomplishmentsScraper;
    NotificationsService notificationsService;
    ObjectMapper objectMapper;
    OkHttpTemplate okHttpTemplate;
    QueryProperties queryProperties;

    public CollectedProfileSourcesDTO scrape(WebDriver webDriver, LinkedinProfileToScrapeDTO profile) {
        try {
            OptionalFieldsContainer optionalFieldsContainer = profile.getOptionalFieldsContainer();
            log.info("Scraping profile: {}", profile.getProfileURL());
            notificationsService.sendAll("LinkedinScraper: Scraping profile with searchResultId: ".concat(Long.toString(profile.getSearchResultId())));
            webDriver.get(profile.getProfileURL());
            CollectedProfileSourcesDTO profileSourcesDTO = new CollectedProfileSourcesDTO();
            profileSourcesDTO.setProfileUrl(profile.getProfileURL());
            profileSourcesDTO.setHeaderSectionSource(headerSectionScraper.scrape(webDriver));
            profileSourcesDTO.setProfilePhotoUrl(headerSectionScraper.scrapeProfilePhotoUrl(webDriver));
            profileSourcesDTO.setAboutSectionSource(aboutSectionScraper.scrape(webDriver));
            profileSourcesDTO.setExperiencesSource(experienceScraper.scrape(webDriver));
            profileSourcesDTO.setEducationsSource(educationScraper.scrape(webDriver));
            profileSourcesDTO.setLicenseSource(scrapeSafe(webDriver, licenseScraper, EMPTY, optionalFieldsContainer.isScrapeLicenses()));
            profileSourcesDTO.setVolunteersSource(scrapeSafe(webDriver, volunteersScraper, EMPTY, optionalFieldsContainer.isScrapeVolunteer()));
            profileSourcesDTO.setSkillsWithEndorsementsSources(scrapeSafe(webDriver, skillsWithEndorsementsScraper, emptyList(), optionalFieldsContainer.isScrapeSkills()));
            profileSourcesDTO.setAllSkillsSource(scrapeSafe(webDriver, allSkillsScraper, EMPTY, optionalFieldsContainer.isScrapeSkills()));
            profileSourcesDTO.setRecommendationsSources(scrapeSafe(webDriver, recommendationsScraper, emptyList(), optionalFieldsContainer.isScrapeRecommendations()));
            profileSourcesDTO.setAccomplishmentsSources(scrapeSafe(webDriver, accomplishmentsScraper, emptyList(), optionalFieldsContainer.isScrapeAccomplishments()));
            profileSourcesDTO.setInterestsSources(scrapeSafe(webDriver, interestsScraper, emptyList(), optionalFieldsContainer.isScrapeInterests()));
            profileSourcesDTO.setActivitiesSources(scrapeSafe(webDriver, activitiesScraper, emptyList(), optionalFieldsContainer.isScrapeActivities()));

            ImageCredentials imageCredentials = new ImageCredentials(profileSourcesDTO.getProfilePhotoUrl(), Long.toString(profile.getDenovoId()));

                Request request = new Request.Builder()
                        .url("http://localhost:8084/api/v1/image/save")
                        .method("POST", RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(imageCredentials)))
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", queryProperties.getToken())
                        .build();
                okHttpTemplate.request(request);

            return profileSourcesDTO;
        } catch (Exception e) {
            log.error("Scraper failed to scrape profile: {}", profile.getProfileURL());
            log.error("Error message: {}", e.getMessage());
            notificationsService.sendAll("LinkedinProfileScraper: failed to scrape profile: ".concat(profile.getProfileURL()));
            notificationsService.sendInternal("LinkedinProfileScraper: Error message: ".concat(e.getMessage()));
            throw new LinkedinScrapingException(e);
        }
    }

    private <T> T scrapeSafe(WebDriver webDriver, Scraper<T> scraper, T defaultValue, boolean optional) {
        return optional ? scraper.scrape(webDriver) : defaultValue;
    }
    @Data
    @AllArgsConstructor
    public static class ImageCredentials {
        private String url;
        private String fileName;
    }
}
