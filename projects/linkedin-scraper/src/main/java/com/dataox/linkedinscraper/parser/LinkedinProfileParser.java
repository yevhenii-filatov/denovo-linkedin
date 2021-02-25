package com.dataox.linkedinscraper.parser;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import com.dataox.linkedinscraper.parser.parsers.*;
import com.dataox.linkedinscraper.parser.utils.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.parser.utils.sources.SkillsSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Override
    public LinkedinProfile parse(CollectedProfileSourcesDTO sourcesDTO) {
        LinkedinProfile linkedinProfile = new LinkedinProfile();

        linkedinProfile.setProfileUrl(sourcesDTO.getProfileUrl());
        linkedinProfile.setLinkedinActivities(activityParser.parse(sourcesDTO.getActivitySource()));
        linkedinProfile.setLinkedinBasicProfileInfo(basicProfileInfoParser.parse(getBasicProfileSource(sourcesDTO)));
        linkedinProfile.setLinkedinEducations(educationParser.parse(sourcesDTO.getEducationsSource()));
        linkedinProfile.setLinkedinSkills(getAllLinkedinSkills(sourcesDTO));
        linkedinProfile.setLinkedinExperiences(experienceParser.parse(sourcesDTO.getExperiencesSource()));
        linkedinProfile.setLinkedinInterests(interestParser.parse(sourcesDTO.getInterestsSources()));
        linkedinProfile.setLinkedinLicenseCertifications(licenseCertificationParser.parse(sourcesDTO.getLicenseSource()));
        linkedinProfile.setLinkedinRecommendations(recommendationParser.parse(sourcesDTO.getRecommendationsSources()));
        linkedinProfile.setLinkedinVolunteerExperiences(volunteerExperienceParser.parse(sourcesDTO.getVolunteersSource()));
        linkedinProfile.setLinkedinAccomplishments(accomplishmentParser.parse(sourcesDTO.getAccomplishmentsSources()));

        return linkedinProfile;
    }

    private List<String> getBasicProfileSource(CollectedProfileSourcesDTO source) {
        String headerSectionSource = source.getHeaderSectionSource();
        String aboutSectionSource = source.getAboutSectionSource();
        return isNotBlank(aboutSectionSource)
                ? List.of(headerSectionSource, aboutSectionSource)
                : Collections.singletonList(headerSectionSource);
    }

    private List<LinkedinSkill> getAllLinkedinSkills(CollectedProfileSourcesDTO source) {
        List<SkillsSource> skillsWithEndorsementsSources = source.getSkillsWithEndorsementsSources();
        String allSkillsSource = source.getAllSkillsSource();

        List<LinkedinSkill> skillsWithEndorsements = skillsWithEndorsementParser.parse(skillsWithEndorsementsSources);
        List<LinkedinSkill> skillsWithoutEndorsements = skillsWithoutEndorsementParser.parse(allSkillsSource);

        return union(skillsWithEndorsements, skillsWithoutEndorsements);
    }
}
