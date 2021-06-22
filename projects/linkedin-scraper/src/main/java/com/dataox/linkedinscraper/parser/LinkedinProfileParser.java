package com.dataox.linkedinscraper.parser;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinParsingException;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import com.dataox.linkedinscraper.parser.parsers.*;
import com.dataox.linkedinscraper.parser.service.validator.ParsingValidator;
import com.dataox.okhttputils.OkHttpTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.union;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LinkedinProfileParser implements LinkedinParser<LinkedinProfile, CollectedProfileSourcesDTO> {
    LinkedinActivityParser activityParser;
    LinkedinBasicProfileInfoParser basicProfileInfoParser;
    LinkedinEducationParser educationParser;
    LinkedinSkillsWithoutEndorsementParser skillsWithoutEndorsementParser;
    LinkedinSkillsWithEndorsementParser skillsWithEndorsementParser;
    LinkedinExperienceParser experienceParser;
    LinkedinInterestParser interestParser;
    LinkedinLicenseCertificationParser licenseCertificationParser;
    LinkedinRecommendationParser recommendationParser;
    LinkedinVolunteerExperienceParser volunteerExperienceParser;
    LinkedinAccomplishmentParser accomplishmentParser;
    ParsingValidator parsingValidator;
    ObjectMapper objectMapper;
    OkHttpTemplate okHttpTemplate;

    @Override
    public LinkedinProfile parse(CollectedProfileSourcesDTO source) {
        try {
            LinkedinProfile linkedinProfile = new LinkedinProfile();
            linkedinProfile.setProfileUrl(source.getProfileUrl());
            linkedinProfile.setLinkedinActivities(activityParser.parse(source.getActivitiesSources()));
            linkedinProfile.setLinkedinBasicProfileInfo(basicProfileInfoParser.parse(getBasicProfileSource(source)));
            linkedinProfile.setLinkedinEducations(educationParser.parse(source.getEducationsSource()));
            linkedinProfile.setLinkedinSkills(getAllLinkedinSkills(source));
            linkedinProfile.setLinkedinExperiences(experienceParser.parse(source.getExperiencesSource()));
            linkedinProfile.setLinkedinInterests(interestParser.parse(source.getInterestsSources()));
            linkedinProfile.setLinkedinLicenseCertifications(licenseCertificationParser.parse(source.getLicenseSource()));
            linkedinProfile.setLinkedinRecommendations(recommendationParser.parse(source.getRecommendationsSources()));
            linkedinProfile.setLinkedinVolunteerExperiences(volunteerExperienceParser.parse(source.getVolunteersSource()));
            linkedinProfile.setLinkedinAccomplishments(accomplishmentParser.parse(source.getAccomplishmentsSources()));

            parsingValidator.validate(linkedinProfile);

//            ImageCredentials imageCredentials = new ImageCredentials(source.getProfilePhotoUrl(), "source.get");
//
//            Request request = new Request.Builder()
//                    .url("http://localhost:8084/api/v1/image/save")
//                    .method("POST", RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(imageCredentials)))
//                    .addHeader("Content-Type", "application/json")
//                    .build();
//            okHttpTemplate.request(request);

            return linkedinProfile;
        } catch (Exception e) {
            throw new LinkedinParsingException(e);
        }
    }

    private List<String> getBasicProfileSource(CollectedProfileSourcesDTO source) {
        String headerSectionSource = source.getHeaderSectionSource();
        String aboutSectionSource = source.getAboutSectionSource();

        return isNotBlank(aboutSectionSource)
                ? List.of(headerSectionSource, aboutSectionSource)
                : List.of(headerSectionSource);
    }

    private List<LinkedinSkill> getAllLinkedinSkills(CollectedProfileSourcesDTO source) {
        List<SkillsSource> skillsWithEndorsementsSources = source.getSkillsWithEndorsementsSources();
        String allSkillsSource = source.getAllSkillsSource();

        List<LinkedinSkill> skillsWithEndorsements = skillsWithEndorsementParser.parse(skillsWithEndorsementsSources);
        List<LinkedinSkill> skillsWithoutEndorsements = skillsWithoutEndorsementParser.parse(allSkillsSource);

        return union(skillsWithEndorsements, skillsWithoutEndorsements);
    }
//    @Data
//    @AllArgsConstructor
//    public static class ImageCredentials {
//        private String url;
//        private String fileName;
//    }
}
