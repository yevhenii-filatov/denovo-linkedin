package com.dataox.linkedinscraper.service.validator;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.sources.InterestsSource;
import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.dto.sources.SkillEndorsementsSource;
import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.dataox.linkedinscraper.service.validator.ValidatorMessage.*;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
public class ScraperValidator {

    // init from db or hardcore data
    public static final String EXPECTED_HEADER = getExpectedHeader();
    public static final String EXPECTED_PHOTO = getExpectedPhoto();
    public static final String EXPECTED_ABOUT = getExpectedAbout();
    public static final String EXPECTED_EXPERIENCE = getExpectedExperience();
    public static final String EXPECTED_EDUCATION = getExpectedEducation();
    public static final String EXPECTED_LICENSE = getExpectedLicense();
    public static final String EXPECTED_VOLUNTEERS = getExpectedVolunteers();
    public static final List<SkillsSource> EXPECTED_SKILLS_WITH = getExpectedSkillsWith();
    public static final String EXPECTED_ALL_SKILLS = getExpectedSkills();
    public static final List<RecommendationsSource> EXPECTED_RECOMMENDATIONS = getExpectedRecommendations();
    public static final List<String> EXPECTED_ACCOMPLISHMENTS = getExpectedAccomplishments();
    public static final List<InterestsSource> EXPECTED_INTERESTS = getExpectedInterests();
    public static final List<String> EXPECTED_ACTIVITIES = getExpectedActivities();

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

    private static String getExpectedHeader() {
        return getResource("header");
    }

    private static String getExpectedPhoto() {
        return "";
    }

    private static String getExpectedAbout() {
        return getResource("about");
    }

    private static String getExpectedExperience() {
        return getResource("experience");
    }

    private static String getExpectedEducation() {
        return getResource("education");
    }

    private static String getExpectedLicense() {
        return getResource("license");
    }

    private static String getExpectedVolunteers() {
        return getResource("volunteers");
    }

    private static List<SkillsSource> getExpectedSkillsWith() {
        return Arrays.asList(
                new SkillsSource("Top Skills and Endorsements",
                        Arrays.asList(new SkillEndorsementsSource(
                                getResource("skillswith/top_skills_and_endorsements/1/url"),
                                getResource("skillswith/top_skills_and_endorsements/1/source")
                                ),
                                new SkillEndorsementsSource(
                                        getResource("skillswith/top_skills_and_endorsements/2/url"),
                                        getResource("skillswith/top_skills_and_endorsements/2/source")
                                ),
                                new SkillEndorsementsSource(
                                        getResource("skillswith/top_skills_and_endorsements/3/url"),
                                        getResource("skillswith/top_skills_and_endorsements/3/source")
                                ))),

                new SkillsSource("Industry Knowledge",
                        Arrays.asList(new SkillEndorsementsSource(
                                getResource("skillswith/industry_knowledge/1/url"),
                                getResource("skillswith/industry_knowledge/1/source")
                        ),
                        new SkillEndorsementsSource(
                                getResource("skillswith/industry_knowledge/2/url"),
                                getResource("skillswith/industry_knowledge/2/source")
                        ))),

                new SkillsSource("Tools & Technologies",
                        Arrays.asList(new SkillEndorsementsSource(
                                getResource("skillswith/tools_technologies/1/url"),
                                getResource("skillswith/tools_technologies/1/source")
                        ),
                        new SkillEndorsementsSource(
                                getResource("skillswith/tools_technologies/2/url"),
                                getResource("skillswith/tools_technologies/2/source")
                        ))),

                new SkillsSource("Other Skills",
                        Collections.singletonList(new SkillEndorsementsSource(
                                getResource("skillswith/other_skills/1/url"),
                                getResource("skillswith/other_skills/1/source")
                        )))
                );
    }

    private static String getExpectedSkills() {
        return getResource("allskills");
    }

    private static List<RecommendationsSource> getExpectedRecommendations() {
        return Collections.singletonList(new RecommendationsSource("GIVEN", getResource("recommendations/GIVEN")));
    }

    private static List<String> getExpectedAccomplishments() {
        return Collections.singletonList(getResource("accomplishments/1"));
    }

    private static List<InterestsSource> getExpectedInterests() {
        return Arrays.asList(
                new InterestsSource("Companies", getResource("interests.Companies")),
                new InterestsSource("Groups", getResource("interests.Companies")),
                new InterestsSource("Influencers", getResource("interests.Influencers")),
                new InterestsSource("Schools", getResource("interests.Schools"))
        );
    }

    private static List<String> getExpectedActivities() {
        List<String> activities = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            activities.add(getResource("activities." + i));
        return activities;
    }

    private static String getResource(String resPath) {
        try {
            String path = String.format("/validator/scraper/%s.html", resPath);
            return IOUtils.resourceToString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void equalsField(Object expected, Object actual, ValidatorMessage exceptionMessage) throws LinkedinValidatorException {
        if (!Objects.deepEquals(expected, actual))
            throw new LinkedinValidatorException(exceptionMessage);
    }
}
