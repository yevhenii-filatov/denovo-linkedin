package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinEducation {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String itemSource;

//    @NotBlank
    private String institutionName;

    private String institutionProfileUrl;

//    @Max(20)
    private String degree;

//    @Max(50)
    private String fieldOfStudy;

//    @Max(20)
    private String grade;

//    @Max(20)
    private String startedYear;

//    @Max(20)
    private String finishedYear;

    private String activitiesAndSocieties;

    private String description;
}
