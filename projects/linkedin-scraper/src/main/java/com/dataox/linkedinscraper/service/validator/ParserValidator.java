package com.dataox.linkedinscraper.service.validator;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dataox.linkedinscraper.service.validator.ValidationType.*;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
@RequiredArgsConstructor
public class ParserValidator {

    public static final String FORMAT = "Expected: %s\nActual: %s";

    private final ObjectMapper mapper;

    public List<ValidationField> checkParser(LinkedinProfile actual) {
        LinkedinProfile expected = getExpectedProfile();

        return Arrays.asList(
                equalsField(expected.getLinkedinActivities(), actual.getLinkedinActivities(), PARSER_ACTIVITIES),
                equalsField(expected.getLinkedinBasicProfileInfo(), actual.getLinkedinBasicProfileInfo(), PARSER_INFO),
                equalsField(expected.getLinkedinEducations(), actual.getLinkedinEducations(), PARSER_EDUCATION),
                equalsField(expected.getLinkedinSkills(), actual.getLinkedinSkills(), PARSER_SKILLS),
                equalsField(expected.getLinkedinExperiences(), actual.getLinkedinExperiences(), PARSER_EXPERIENCE),
                equalsField(expected.getLinkedinInterests(), actual.getLinkedinInterests(), PARSER_INTERESTS),
                equalsField(expected.getLinkedinLicenseCertifications(), actual.getLinkedinLicenseCertifications(), PARSER_LICENSE),
                equalsField(expected.getLinkedinRecommendations(), actual.getLinkedinRecommendations(), PARSER_RECOMMENDATIONS),
                equalsField(expected.getLinkedinVolunteerExperiences(), actual.getLinkedinVolunteerExperiences(), PARSER_VOLUNTEERS),
                equalsField(expected.getLinkedinAccomplishments(), actual.getLinkedinAccomplishments(), PARSER_ACCOMPLISHMENTS));
    }

    private LinkedinProfile getExpectedProfile() {
        try {
            return mapper.readValue(getClass().getResource("/validator/parser/parsedprofile.json"), LinkedinProfile.class);
        } catch (IOException e) {
            throw new LinkedinValidatorException(e.getMessage());
        }
    }

    private ValidationField equalsField(Object expected, Object actual, ValidationType type) {
        if (Objects.deepEquals(expected, actual)) return null;
        else return new ValidationField(type, String.format(FORMAT, expected.toString(), actual.toString()));
    }
}
