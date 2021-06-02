package com.dataox.linkedinscraper.selfvalidator;

import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class LinkedinSelfValidatorScheduler {

    private static final String VALIDATION_FORMAT = "Validator_Notification\n" +
            "Not equals fields: [%s]";
    private static final String EXCEPTION_FORMAT = "Validator_Notification\n" +
            "Has occurred exception while validation with message: [%s]";

    private final LinkedinSelfValidator validator;
    private final NotificationsService notificationsService;

    //    @Scheduled(cron = "0 0 * * * *")
    public void checkLinkedin(){
        String message = "Validator_Notification\n" + "No changes.";
        try {
            List<ValidationField> validationResult = validator.validate();
            if (validationResult.size() > 0) {
                message = String.format(EXCEPTION_FORMAT, validationResult);
            }
        } catch (LinkedinSelfValidatorException e) {
            message = String.format(EXCEPTION_FORMAT, e.getMessage());
            log.warn(message);
        }
        notificationsService.sendInternal(message);
    }

}
