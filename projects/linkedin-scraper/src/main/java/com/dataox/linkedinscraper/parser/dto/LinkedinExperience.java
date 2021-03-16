package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinExperience {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    private String jobType;

    @NotBlank
    @Max(100)
    private String companyName;

    @NotBlank
    private String companyProfileUrl;

    @NotBlank
    @Max(100)
    private String position;

    @NotBlank
    @Max(20)
    private String dateStarted;

    @NotBlank
    @Max(20)
    private String dateFinished;

    @Max(20)
    private String totalDuration;

    @Max(100)
    private String location;

    private String description;
}
