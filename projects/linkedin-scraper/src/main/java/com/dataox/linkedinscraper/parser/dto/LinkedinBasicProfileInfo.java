package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class LinkedinBasicProfileInfo {

    //    @NotNull
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Instant updatedAt;

    //    @NotBlank
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String headerSectionSource;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String aboutSectionSource;

    //    @NotBlank
//    @Max(255)
    private String fullName;

    //    @NotBlank
//    @Max(10)
    private String numberOfConnections;

    //    @NotBlank
//    @Max(100)
    private String location;

    private String cachedImageUrl;

    private String about;
}
