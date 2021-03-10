package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LinkedinPost {

    private String url;

    @NotBlank
    private String itemSource;

    @NotBlank
    @Max(20)
    private String relativePublicationDate;

    @NotNull
    private Instant collectedDate;

    @NotNull
    private Instant absolutePublicationDate;

    @EqualsAndHashCode.Include
    @NotBlank
    private String authorProfileUrl;

    @NotBlank
    private String authorConnectionDegree;

    @NotBlank
    private String authorHeadline;

    @EqualsAndHashCode.Include
    private String content;

    private int numberOfComments;

    private int numberOfReactions;

    private Set<LinkedinComment> linkedinComments = new HashSet<>();
}
