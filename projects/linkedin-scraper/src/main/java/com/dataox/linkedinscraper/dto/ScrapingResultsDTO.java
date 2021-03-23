package com.dataox.linkedinscraper.dto;

import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ScrapingResultsDTO {
    List<LinkedinProfile> successfulProfiles;
    List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles;
}
