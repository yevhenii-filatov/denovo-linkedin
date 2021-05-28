package com.dataox.linkedinscraper.service.validator;

import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class LinkedinValidatorScheduler {

    private static final String MESSAGE_FORMAT = "Has occurred problem with %s";

    private final LinkedinValidator validator;
    private final NotificationsService notificationsService;

//    @Scheduled(cron = "0 0 * * * *")
    public void checkLinkedin(){
        try {
            validator.validate();
        }catch (LinkedinValidatorException e){
            String message = String.format(MESSAGE_FORMAT, e.getMessage());
            log.warn(message);
            notificationsService.sendInternal(message);
        }
    }

}
