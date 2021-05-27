package com.dataox.linkedinscraper.service.validator;

import com.dataox.linkedinscraper.parser.dto.*;
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
public class ParserValidator {
    private static final List<LinkedinActivity> EXPECTED_ACTIVITIES = new ArrayList<>();
    private static final LinkedinBasicProfileInfo EXPECTED_INFO = new LinkedinBasicProfileInfo();
    private static final List<LinkedinEducation> EXPECTED_EDUCATION = new ArrayList<>();
    private static final List<LinkedinSkill> EXPECTED_SKILLS = new ArrayList<>();
    private static final List<LinkedinExperience> EXPECTED_EXPERIENCE = new ArrayList<>();
    private static final List<LinkedinInterest> EXPECTED_INTERESTS = new ArrayList<>();
    private static final List<LinkedinLicenseCertification> EXPECTED_LICENSE = new ArrayList<>();
    private static final List<LinkedinRecommendation> EXPECTED_RECOMMENDATIONS = new ArrayList<>();
    private static final List<LinkedinVolunteerExperience> EXPECTED_VOLUNTEERS = new ArrayList<>();
    private static final List<LinkedinAccomplishment> EXPECTED_ACCOMPLISHMENTS = new ArrayList<>();

    public void checkParser(LinkedinProfile profile) throws LinkedinValidatorException {
        equalsField(EXPECTED_ACTIVITIES, profile.getLinkedinActivities(), PARSER_ACTIVITIES);
        equalsField(EXPECTED_INFO, profile.getLinkedinBasicProfileInfo(), PARSER_INFO);
        equalsField(EXPECTED_EDUCATION, profile.getLinkedinEducations(), PARSER_EDUCATION);
        equalsField(EXPECTED_SKILLS, profile.getLinkedinSkills(), PARSER_SKILLS);
        equalsField(EXPECTED_EXPERIENCE, profile.getLinkedinExperiences(), PARSER_EXPERIENCE);
        equalsField(EXPECTED_INTERESTS, profile.getLinkedinInterests(), PARSER_INTERESTS);
        equalsField(EXPECTED_LICENSE, profile.getLinkedinLicenseCertifications(), PARSER_LICENSE);
        equalsField(EXPECTED_RECOMMENDATIONS, profile.getLinkedinRecommendations(), PARSER_RECOMMENDATIONS);
        equalsField(EXPECTED_VOLUNTEERS, profile.getLinkedinVolunteerExperiences(), PARSER_VOLUNTEERS);
        equalsField(EXPECTED_ACCOMPLISHMENTS, profile.getLinkedinAccomplishments(), PARSER_ACCOMPLISHMENTS);
    }

    private void equalsField(Object expected, Object actual, ValidatorMessage exceptionMessage) throws LinkedinValidatorException {
        if (Objects.deepEquals(expected, actual))
            throw new LinkedinValidatorException(String.format("parsing %s", exceptionMessage));
    }
}
