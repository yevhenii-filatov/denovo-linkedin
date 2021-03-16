package com.dataox.linkedinscraper.dto.sources;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 25/02/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SkillEndorsementsSource {
    String skillUrl;
    String skillSource;
}
