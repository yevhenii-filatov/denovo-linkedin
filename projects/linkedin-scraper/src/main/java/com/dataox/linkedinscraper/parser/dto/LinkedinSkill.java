package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource"})
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
