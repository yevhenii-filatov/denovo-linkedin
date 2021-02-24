package com.dataox.linkedinscraper.parser.utils.sources;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillsSource {
    String category;
    List<String> skillsWithEndorsementsSources;
}
