package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinBasicProfileInfo {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String headerSectionSource;

    @NotBlank
    private String aboutSectionSource;

    @NotBlank
    private String fullName;

    @NotBlank
    private String numberOfConnections;

    private String cachedImageUrl;

    private String about;
}
