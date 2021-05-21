package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LinkedinComment {

    private String url;

    @NotBlank
    private String itemSource;

    @NotBlank
    @EqualsAndHashCode.Include
    private String content;

    @NotBlank
//    @Max(20)
    private String relativePublicationDate;

    @NotNull
    private Instant collectedDate;

    @NotNull
    private Instant absolutePublicationDate;

    @EqualsAndHashCode.Include
    private int numberOfReactions;

    @EqualsAndHashCode.Include
    private int numberOfReplies;
}
