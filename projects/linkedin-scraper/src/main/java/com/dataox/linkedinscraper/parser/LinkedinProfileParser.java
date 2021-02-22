package com.dataox.linkedinscraper.parser;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.parser.parsers.*;
import com.dataox.linkedinscraper.parser.utils.CollectedProfileSourcesDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.union;

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

    @Override
    public LinkedinProfile parse(CollectedProfileSourcesDTO source) {
        LinkedinProfile linkedinProfile = new LinkedinProfile();

        linkedinProfile.setProfileUrl(source.getProfileUrl());
        linkedinProfile.setLinkedinActivities(activityParser.parse(source.getPostUrlAndActivitySource()));
        linkedinProfile.setLinkedinBasicProfileInfo(
                basicProfileInfoParser.parse(List.of(source.getHeaderSectionSource(), source.getAboutSectionSource()))
        );
        linkedinProfile.setLinkedinEducations(educationParser.parse(source.getEducationsSource()));
        linkedinProfile.setLinkedinSkills(
                union(
                        skillsWithEndorsementParser.parse(source.getSkillsWithEndorsementsSource()),
                        skillsWithoutEndorsementParser.parse(source.getAllSkillsSource())
                )
        );
        linkedinProfile.setLinkedinExperiences(experienceParser.parse(source.getExperiencesSource()));
        linkedinProfile.setLinkedinInterests(interestParser.parse(source.getInterestsSources()));
        linkedinProfile.setLinkedinLicenseCertifications(licenseCertificationParser.parse(source.getLicenseSource()));
        linkedinProfile.setLinkedinRecommendations(recommendationParser.parse(source.getRecommendationsSource()));
        linkedinProfile.setLinkedinVolunteerExperiences(volunteerExperienceParser.parse(source.getVolunteersSource()));

        return linkedinProfile;
    }
}
