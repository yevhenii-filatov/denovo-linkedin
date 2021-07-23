package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author Dmitriy Lysko
 * @since 26/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionalFieldsContainer implements Serializable {
    private static final long serialVersionUID = 2L;
    boolean scrapeLicenses;
    boolean scrapeVolunteer;
    boolean scrapeInterests;
    boolean scrapeRecommendations;
    boolean scrapeAccomplishments;
    boolean scrapeSkills;
    boolean scrapeActivities;
}
