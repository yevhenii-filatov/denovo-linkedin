package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinAccomplishment {

    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private String type;

}
