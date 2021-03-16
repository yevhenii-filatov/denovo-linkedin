package com.dataox.linkedinscraper.dto.sources;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class InterestsSource {
    String category;
    String source;
}
