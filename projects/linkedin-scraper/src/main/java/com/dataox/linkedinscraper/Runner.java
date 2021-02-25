package com.dataox.linkedinscraper;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 24/02/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {
    private final LoginService loginService;
    private final LinkedinProfileScraper scraper;
    private final LinkedinProfileParser parser;
    private final ChromeOptions chromeOptions;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> profileUrl = Arrays.asList(
//                "https://www.linkedin.com/in/daymesanchez/"
//                "https://www.linkedin.com/in/ashley-scheer-3b4155b/"
//                "https://www.linkedin.com/in/lisa-umans-18350465/"
//                "https://www.linkedin.com/in/winniedotwong/"
//                "https://www.linkedin.com/in/evelyn-sahr-2615b212/"
//                "https://www.linkedin.com/in/michael-mason-5196898/"
//                "https://www.linkedin.com/in/clairebendix/"
//                "https://www.linkedin.com/in/andykgordon/"
//                "https://www.linkedin.com/in/markostrau/"
                "https://www.linkedin.com/in/davidjohnball/"
        );
        List<CollectedProfileSourcesDTO> sourcesDTOS = new ArrayList<>();
        ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions);
        try (launcher) {
            WebDriver webDriver = launcher.getWebDriver();
            loginService.performLogin(webDriver);
            for (String profileURL : profileUrl) {
                sourcesDTOS.add(scraper.scrape(webDriver, profileURL));
            }
        }
        List<LinkedinProfile> profiles = new ArrayList<>();
        for (CollectedProfileSourcesDTO sourcesDTO : sourcesDTOS) {
            log.info("Parsing profile {}", sourcesDTO.getProfileUrl());
            LinkedinProfile parse = parser.parse(sourcesDTO);
            profiles.add(parse);
        }
        List<String> profileJsons = new ArrayList<>();
        for (LinkedinProfile profile : profiles) {
            removeSources(profile);
            profileJsons.add(objectMapper.writeValueAsString(profile));
        }
        System.out.println(profileJsons);
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
