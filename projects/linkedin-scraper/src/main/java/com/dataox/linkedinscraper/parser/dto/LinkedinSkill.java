package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkedinSkill {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String itemSource;

//    @NotBlank
//    @Max(100)
    private String name;

//    @NotBlank
//    @Max(100)
    private String category;

    private String url;

    private int numberOfEndorsements;

    private List<LinkedinEndorsement> linkedinEndorsements = new ArrayList<>();
}
