package com.dataox.linkedinscraper;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.service.ScrapeLinkedinProfileService;
import com.dataox.notificationservice.service.NotificationsService;
import com.dataox.okhttputils.OkHttpTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 04/03/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {
    private final ScrapeLinkedinProfileService scrapeLinkedinProfileService;
    private final NotificationsService notificationsService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final OkHttpTemplate okHttpTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        notificationsService.sendAll("LinkedIn Scraper Applicaton has been started.");
        while (scrapeLinkedinProfileService.isWorking()) {
            List<LinkedinProfileToScrapeDTO> profileToScrapeDTOS = (List<LinkedinProfileToScrapeDTO>) rabbitTemplate.receiveAndConvert();
            while (isNull(profileToScrapeDTOS)) {
                profileToScrapeDTOS = (List<LinkedinProfileToScrapeDTO>) rabbitTemplate.receiveAndConvert();
                Thread.sleep(500);
            }
            log.info("Received {} profiles to scrape", profileToScrapeDTOS.size());
            objectMapper.registerModule(new JavaTimeModule());
            ScrapingResultsDTO scrape = scrapeLinkedinProfileService.scrape(profileToScrapeDTOS);
            Request request = new Request.Builder()
                    .url("https://localhost:8080/api/v1/scraping/receive/scraped")
                    .method("POST", RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(scrape)))
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                okHttpTemplate.request(request);
            } catch (Exception e) {
                notificationsService.sendAll("LinkedIn Scraper Applicaton has unexpectedly finished it's work. Check logs for detailed information.");
                notificationsService.sendInternal(e.getMessage() + "-----" +ExceptionUtils.getStackTrace(e));
                log.error("LinkedInScraper: Error has been occured: {}", e.getMessage());
            }
        }
    }

    private void removeSources(LinkedinProfile profile) {
        profile.getLinkedinBasicProfileInfo().setHeaderSectionSource("");
        profile.getLinkedinBasicProfileInfo().setAboutSectionSource("");
        profile.getLinkedinActivities().forEach(linkedinActivity -> {
            linkedinActivity.getLinkedinPost().setItemSource("");
            linkedinActivity.getLinkedinPost().getLinkedinComments().forEach(linkedinComment -> linkedinComment.setItemSource(""));
        });
        profile.getLinkedinEducations().forEach(linkedinEducation -> linkedinEducation.setItemSource(""));
        profile.getLinkedinSkills().forEach(linkedinSkill -> {
            linkedinSkill.setItemSource("");
            linkedinSkill.getLinkedinEndorsements().forEach(linkedinEndorsement -> linkedinEndorsement.setItemSource(""));
        });
        profile.getLinkedinExperiences().forEach(linkedinExperience -> linkedinExperience.setItemSource(""));
        profile.getLinkedinInterests().forEach(linkedinInterest -> linkedinInterest.setItemSource(""));
        profile.getLinkedinLicenseCertifications().forEach(linkedinLicenseCertification -> linkedinLicenseCertification.setItemSource(""));
        profile.getLinkedinRecommendations().forEach(linkedinRecommendation -> linkedinRecommendation.setItemSource(""));
        profile.getLinkedinVolunteerExperiences().forEach(linkedinVolunteerExperience -> linkedinVolunteerExperience.setItemSource(""));
        profile.getLinkedinAccomplishments().forEach(linkedinAccomplishment -> linkedinAccomplishment.setItemSource(""));
    }
}
