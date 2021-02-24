package com.dataox.linkedinscraper.parser.utils.sources;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendationsSource {
    String type;
    String source;
}
