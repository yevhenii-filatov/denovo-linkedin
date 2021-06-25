package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinAccomplishmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource"})
@ToString(exclude = {"updatedAt", "itemSource"})
@NoArgsConstructor
public class LinkedinAccomplishment {

    //    @NotNull
    private Instant updatedAt;

    //    @NotBlank
    private String itemSource;

    //    @NotBlank
    private String title;

    private String description;

    //    @NotNull
    private LinkedinAccomplishmentType linkedinAccomplishmentType;
}
