package com.dataox.linkedinscraper.dto;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkedinProfilesDTO {
    List<LinkedinProfile> successfulProfiles;
    List<String> unavailableProfileUrls;
    List<String> failedToScrapeProfileUrls;
}
