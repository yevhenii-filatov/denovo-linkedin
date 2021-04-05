package com.dataox.linkedinscraper.dto;

import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
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
    @NotNull
    List<LinkedinProfile> successfulProfiles;
    @NotNull
    List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles;
}
