package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class LinkedinBasicProfileInfo {

    //    @NotNull
    @EqualsAndHashCode.Exclude
    private Instant updatedAt;

    //    @NotBlank
    @EqualsAndHashCode.Exclude
    private String headerSectionSource;

    @EqualsAndHashCode.Exclude
    private String aboutSectionSource;

    //    @NotBlank
//    @Max(255)
    private String fullName;

    //    @NotBlank
//    @Max(10)
    @EqualsAndHashCode.Exclude
    private String numberOfConnections;

    //    @NotBlank
//    @Max(100)
    private String location;

    private String cachedImageUrl;

    private String about;
}
