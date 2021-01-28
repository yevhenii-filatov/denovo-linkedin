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
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CollectedProfileSourcesDTO {
    String headerSectionSource;
    String aboutSectionSource;
    String educationsSource;
    String experiencesSource;
    String recommendationsSource;
    String skillsSource;
    String licenseSource;
    String volunteersSource;
    Map<String,String> interestsSources;
    List<String> activitiesSources;
}
