package com.dataox.linkedinscraper.parser.service.validator;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.parser.exceptions.ParsingValidationException;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParsingValidatorImpl implements ParsingValidator {

    private final NotificationsService notificationsService;
    private final Validator validator;
    private static final String COMMON_MESSAGE = "Parsing validation failed for -> ";

    @Override
    public void validate(LinkedinProfile profile) {
        Set<ConstraintViolation<LinkedinProfile>> violationSet = validator.validate(profile);
        if (!violationSet.isEmpty()) {
            List<String> violations = getFormattedViolations(violationSet);
            log.error(COMMON_MESSAGE + "{} due to: {}", profile.getProfileUrl(), violations);
            notificationsService.sendExternal(COMMON_MESSAGE + profile.getProfileUrl());
            notificationsService.sendInternal(COMMON_MESSAGE + profile.getProfileUrl() + " due to: " + violations);
            throw new ParsingValidationException(violations.toString());
        }
        log.info("Parsing validation for -> {} was successful", profile);
    }

    private List<String> getFormattedViolations(Set<ConstraintViolation<LinkedinProfile>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.toList());
    }
}
