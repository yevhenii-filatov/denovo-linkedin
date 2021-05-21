package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinInterestType;
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
//    @Max(100)
    private String name;

    @NotNull
    private LinkedinInterestType linkedinInterestType;

    @NotBlank
    private String profileUrl;

    private String headline;

    @NotBlank
//    @Max(50)
    private String numberOfFollowers;
}
