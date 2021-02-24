package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinVolunteerExperience {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String companyName;

    private String companyProfileUrl;

    @NotBlank
    private String position;

    @NotBlank
    private String dateStarted;

    @NotBlank
    private String dateFinished;

    @NotBlank
    private String totalDuration;

    private String fieldOfActivity;

    private String description;
}
