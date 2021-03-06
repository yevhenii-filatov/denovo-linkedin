package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinRecommendation {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotNull
    private String type;

    @NotBlank
    @Max(255)
    private String personFullName;

    @NotBlank
    private String personProfileUrl;

    @NotBlank
    private String personHeadline;

    @NotBlank
    private String personExtraInfo;

    @NotBlank
    private String description;
}
