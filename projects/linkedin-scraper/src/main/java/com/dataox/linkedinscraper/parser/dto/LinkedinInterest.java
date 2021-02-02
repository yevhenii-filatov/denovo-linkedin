package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.types.LinkedinInterestType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinInterest {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String name;

    @NotNull
    private LinkedinInterestType linkedinInterestType;

    @NotBlank
    private String profileUrl;

    private String headline;

    @NotBlank
    private String numberOfFollowers;
}
