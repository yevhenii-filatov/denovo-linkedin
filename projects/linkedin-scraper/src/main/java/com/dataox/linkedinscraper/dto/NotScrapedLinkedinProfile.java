package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 02/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class NotScrapedLinkedinProfile {
    String profileURL;
    String errorDescription;
    boolean reusable;
}
