package com.dataox.linkedinscraper.parser.utils;


import com.dataox.linkedinscraper.parser.utils.sources.InterestsSource;
import com.dataox.linkedinscraper.parser.utils.sources.RecommendationsSource;
import com.dataox.linkedinscraper.parser.utils.sources.SkillsSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectedProfileSourcesDTO {
    String profileUrl;
    String profilePhotoUrl;
    String headerSectionSource;
    String aboutSectionSource;
    String experiencesSource;
    String educationsSource;
    String licenseSource;
    String volunteersSource;
    String allSkillsSource;
    List<String> accomplishmentsSources;
    List<SkillsSource> skillsWithEndorsementsSources;
    List<RecommendationsSource> recommendationsSources;
    List<InterestsSource> interestsSources;
    List<String> activitySource;
}
