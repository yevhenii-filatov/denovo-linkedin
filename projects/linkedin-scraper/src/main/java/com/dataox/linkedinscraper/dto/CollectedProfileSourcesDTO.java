package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectedProfileSourcesDTO {
    String headerSectionSource;
    String aboutSectionSource;
    String experiencesSource;
    String educationsSource;
    String licenseSource;
    String volunteersSource;
    String allSkillsSource;
    List<String> accomplishmentsSources;
    Map<String, List<String>> skillsWithEndorsementsSource;
    Map<String, String> recommendationsSource;
    Map<String, String> interestsSources;
    Map<String, String> urlAndActivitiesSources;
}
