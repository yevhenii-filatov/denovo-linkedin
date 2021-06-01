package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource"})
@NoArgsConstructor
public class LinkedinEndorsement {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String itemSource;

//    @NotBlank
//    @Max(100)
    private String endorserFullName;

    private String endorserHeadline;

//    @NotBlank
//    @Max(10)
    private String endorserConnectionDegree;

//    @NotBlank
    private String endorserProfileUrl;
}
