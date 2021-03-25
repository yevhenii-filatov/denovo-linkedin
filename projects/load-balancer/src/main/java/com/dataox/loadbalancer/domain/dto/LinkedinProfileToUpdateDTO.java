package com.dataox.loadbalancer.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 25/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LinkedinProfileToUpdateDTO {
    Long linkedinProfileId;
    boolean scrapeLicenses;
    boolean scrapeVolunteer;
    boolean scrapeInterests;
    boolean scrapeRecommendations;
    boolean scrapeAccomplishments;
    boolean scrapeSkills;
    boolean scrapeActivities;
}
