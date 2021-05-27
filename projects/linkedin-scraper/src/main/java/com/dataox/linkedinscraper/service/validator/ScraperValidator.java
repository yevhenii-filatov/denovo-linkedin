package com.dataox.linkedinscraper.service.validator;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.sources.InterestsSource;
import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dataox.linkedinscraper.service.validator.ValidatorMessage.*;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
public class ScraperValidator {

    public static final String EXPECTED_HEADER = "";
    public static final String EXPECTED_PHOTO = "";
    public static final String EXPECTED_ABOUT = "";
    public static final String EXPECTED_EXPERIENCE = "";
    public static final String EXPECTED_EDUCATION = "";
    public static final String EXPECTED_LICENSE = "";
    public static final String EXPECTED_VOLUNTEERS = "";
    public static final List<SkillsSource> EXPECTED_SKILLS_WITH = new ArrayList<>();
    public static final String EXPECTED_ALL_SKILLS = "";
    public static final List<RecommendationsSource> EXPECTED_RECOMMENDATIONS = new ArrayList<>();
    public static final List<String> EXPECTED_ACCOMPLISHMENTS = new ArrayList<>();
    public static final List<InterestsSource> EXPECTED_INTERESTS = new ArrayList<>();
    public static final List<String> EXPECTED_ACTIVITIES = new ArrayList<>();

    public void checkScraper(CollectedProfileSourcesDTO profile) throws LinkedinValidatorException {
        equalsField(EXPECTED_HEADER, profile.getHeaderSectionSource(), SCRAPED_HEADER);
        equalsField(EXPECTED_PHOTO, profile.getProfilePhotoUrl(), SCRAPED_PHOTO);
        equalsField(EXPECTED_ABOUT, profile.getAboutSectionSource(), SCRAPED_ABOUT);
        equalsField(EXPECTED_EXPERIENCE, profile.getExperiencesSource(), SCRAPED_EXPERIENCE);
        equalsField(EXPECTED_EDUCATION, profile.getEducationsSource(), SCRAPED_EDUCATION);
        equalsField(EXPECTED_LICENSE, profile.getLicenseSource(), SCRAPED_LICENSE);
        equalsField(EXPECTED_VOLUNTEERS, profile.getVolunteersSource(), SCRAPED_VOLUNTEERS);
        equalsField(EXPECTED_SKILLS_WITH, profile.getSkillsWithEndorsementsSources(), SCRAPED_SKILLS);
        equalsField(EXPECTED_ALL_SKILLS, profile.getAllSkillsSource(), SCRAPED_ALL_SKILLS);
        equalsField(EXPECTED_RECOMMENDATIONS, profile.getRecommendationsSources(), SCRAPED_RECOMMENDATIONS);
        equalsField(EXPECTED_ACCOMPLISHMENTS, profile.getAccomplishmentsSources(), SCRAPED_ACCOMPLISHMENTS);
        equalsField(EXPECTED_INTERESTS, profile.getInterestsSources(), SCRAPED_INTERESTS);
        equalsField(EXPECTED_ACTIVITIES, profile.getActivitiesSources(), SCRAPED_ACTIVITIES);
    }

    private void equalsField(Object expected, Object actual, ValidatorMessage exceptionMessage) throws LinkedinValidatorException {
        if (Objects.deepEquals(expected, actual))
            throw new LinkedinValidatorException(String.format("scraping %s", exceptionMessage));
    }
}
