package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinEndorsement {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String endorserFullName;

    private String endorserHeadline;

    @NotBlank
    private String endorserConnectionDegree;

    @NotBlank
    private String endorserProfileUrl;
}
