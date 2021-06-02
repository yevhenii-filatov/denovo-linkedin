package com.dataox.linkedinscraper.validator;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.sources.InterestsSource;
import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dataox.linkedinscraper.validator.ValidationType.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Slf4j
@Service
public class ScraperValidator {

    private static final String EMPTY = "Empty";

    public List<ValidationField> checkScraper(CollectedProfileSourcesDTO profile) throws LinkedinValidatorException {
        return Arrays.asList(
                checkAboutSection(profile.getAboutSectionSource()),
                checkHeaderSection(profile.getHeaderSectionSource()),
                checkProfilePhotoUrl(profile.getProfilePhotoUrl()),
                checkExperiencesSource(profile.getExperiencesSource()),
                checkEducationsSource(profile.getEducationsSource()),
                checkLicenseSource(profile.getLicenseSource()),
                checkVolunteersSource(profile.getVolunteersSource()),
                checkSkillsWithEndorsementsSources(profile.getSkillsWithEndorsementsSources()),
                checkAllSkillsSource(profile.getAllSkillsSource()),
                checkRecommendationsSources(profile.getRecommendationsSources()),
                checkAccomplishmentsSources(profile.getAccomplishmentsSources()),
                checkInterestsSources(profile.getInterestsSources()),
                checkActivitiesSources(profile.getActivitiesSources())
        );
    }

    private ValidationField checkAboutSection(String aboutSectionSource) {
        if (isNotBlank(aboutSectionSource)) return null;
        else return new ValidationField(SCRAPED_VOLUNTEERS, EMPTY);
    }

    private ValidationField checkActivitiesSources(List<String> activitiesSources) {
        if (!activitiesSources.isEmpty() && activitiesSources.stream().allMatch(StringUtils::isNotBlank)) return null;
        else return new ValidationField(SCRAPED_ACTIVITIES, EMPTY);
    }

    private ValidationField checkInterestsSources(List<InterestsSource> interestsSources) {
        if (!interestsSources.isEmpty() && interestsSources.stream().allMatch(Objects::nonNull)) return null;
        else return new ValidationField(SCRAPED_INTERESTS, EMPTY);
    }

    private ValidationField checkAccomplishmentsSources(List<String> accomplishmentsSources) {
        if(!accomplishmentsSources.isEmpty() && accomplishmentsSources.stream().allMatch(StringUtils::isNotBlank)) return null;
        else return new ValidationField(SCRAPED_ACCOMPLISHMENTS, EMPTY);
    }

    private ValidationField checkRecommendationsSources(List<RecommendationsSource> recommendationsSources) {
        if(!recommendationsSources.isEmpty() && recommendationsSources.stream().allMatch(Objects::nonNull)) return null;
        else return new ValidationField(SCRAPED_RECOMMENDATIONS, EMPTY);
    }

    private ValidationField checkAllSkillsSource(String allSkillsSource) {
        if(isNotBlank(allSkillsSource)) return null;
        else return new ValidationField(SCRAPED_ALL_SKILLS, EMPTY);
    }

    private ValidationField checkSkillsWithEndorsementsSources(List<SkillsSource> skillsWithEndorsementsSources) {
        if(!skillsWithEndorsementsSources.isEmpty() && skillsWithEndorsementsSources.stream().allMatch(Objects::nonNull)) return null;
        else return new ValidationField(SCRAPED_SKILLS, EMPTY);
    }

    private ValidationField checkVolunteersSource(String volunteersSource) {
        if(isNotBlank(volunteersSource)) return null;
        else return new ValidationField(SCRAPED_VOLUNTEERS, EMPTY);
    }

    private ValidationField checkLicenseSource(String licenseSource) {
        if(isNotBlank(licenseSource)) return null;
        else return new ValidationField(SCRAPED_LICENSE, EMPTY);
    }

    private ValidationField checkEducationsSource(String educationsSource) {
        if(isNotBlank(educationsSource)) return null;
        else return new ValidationField(SCRAPED_EDUCATION, EMPTY);
    }

    private ValidationField checkExperiencesSource(String experiencesSource) {
        if(isNotBlank(experiencesSource)) return null;
        else return new ValidationField(SCRAPED_EXPERIENCE, EMPTY);
    }

    private ValidationField checkProfilePhotoUrl(String profilePhotoUrl) {
        if(isNotBlank(profilePhotoUrl)) return null;
        else return new ValidationField(SCRAPED_PHOTO, EMPTY);
    }

    private ValidationField checkHeaderSection(String headerSectionSource) {
        if(isNotBlank(headerSectionSource)) return null;
        else return new ValidationField(SCRAPED_HEADER, EMPTY);
    }
}
