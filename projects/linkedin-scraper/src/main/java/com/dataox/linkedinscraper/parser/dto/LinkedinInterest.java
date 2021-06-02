package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinInterestType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource", "numberOfFollowers"})
@ToString(exclude = {"updatedAt", "itemSource", "numberOfFollowers"})
@NoArgsConstructor
public class LinkedinInterest {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String itemSource;

//    @NotBlank
//    @Max(100)
    private String name;

//    @NotNull
    private LinkedinInterestType linkedinInterestType;

//    @NotBlank
    private String profileUrl;

    private String headline;

//    @NotBlank
//    @Max(50)
    private String numberOfFollowers;
}
