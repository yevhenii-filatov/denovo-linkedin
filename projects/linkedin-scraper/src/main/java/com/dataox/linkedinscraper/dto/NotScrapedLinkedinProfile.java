package com.dataox.linkedinscraper.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dmitriy Lysko
 * @since 02/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotScrapedLinkedinProfile {
    String profileURL;
    String errorDescription;
    boolean notReusable;
}
