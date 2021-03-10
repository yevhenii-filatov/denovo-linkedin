package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String companyName;

    @NotBlank
    private String companyProfileUrl;

    @NotBlank
    private String position;

    @NotBlank
    private String dateStarted;

    @NotBlank
    private String dateFinished;

    private String totalDuration;

    private String location;

    private String description;
}
