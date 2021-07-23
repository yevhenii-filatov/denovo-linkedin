package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource"})
@ToString(exclude = {"updatedAt", "itemSource"})
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
