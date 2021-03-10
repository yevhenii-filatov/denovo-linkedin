package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinEducation {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String institutionName;

    private String institutionProfileUrl;

    private String degree;

    private String fieldOfStudy;

    private String grade;

    private String startedYear;

    private String finishedYear;

    private String activitiesAndSocieties;

    private String description;
}
