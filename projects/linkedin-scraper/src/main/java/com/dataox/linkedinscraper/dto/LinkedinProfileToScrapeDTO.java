package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author Dmitriy Lysko
 * @since 02/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkedinProfileToScrapeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    Long searchResultId;
    String profileURL;
    boolean scrapeLicenses;
    boolean scrapeVolunteer;
    boolean scrapeInterests;
    boolean scrapeRecommendations;
    boolean scrapeAccomplishments;
    boolean scrapeSkills;
    boolean scrapeActivities;
}
