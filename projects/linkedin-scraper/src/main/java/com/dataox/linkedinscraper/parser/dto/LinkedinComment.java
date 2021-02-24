package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinComment {

    private String url;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String content;

    @NotBlank
    private String relativePublicationDate;

    @NotNull
    private Instant collectedDate;

    @NotNull
    private Instant absolutePublicationDate;

    private int numberOfReactions;

    private int numberOfReplies;
}
