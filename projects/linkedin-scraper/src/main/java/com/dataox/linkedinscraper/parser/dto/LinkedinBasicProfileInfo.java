package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinBasicProfileInfo {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String headerSectionSource;

    private String aboutSectionSource;

    @NotBlank
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
