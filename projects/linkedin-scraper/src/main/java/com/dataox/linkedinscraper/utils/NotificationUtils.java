package com.dataox.linkedinscraper.utils;

import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import lombok.experimental.UtilityClass;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@UtilityClass
public class NotificationUtils {

    public static String createExceptionMessage(Exception e, String profileUrl,String applicationName) {
        return String.format("Scraper name: %s%nException occurred while scraping profile %s%nException name: %s%nException message:%s",
                applicationName,
                profileUrl,
                e.getClass().getSimpleName(),
                e.getMessage());
    }

    public static String createLinkedinErrorMessage(LinkedinError linkedinError, String applicationName) {
        return String.format("Scraper name:%s%nLinkedin error occurred%nMessage: %s",
                applicationName,
                linkedinError.getMessage());
    }

}
