package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkedinPost {

    private String url;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String relativePublicationDate;

    @NotNull
    private Instant collectedDate;

    @NotNull
    private Instant absolutePublicationDate;

    @NotBlank
    private String authorProfileUrl;

    @NotBlank
    private String authorConnectionDegree;

    @NotBlank
    private String authorHeadline;

    private String content;

    private int numberOfComments;

    private int numberOfReactions;

    private List<LinkedinComment> linkedinComments = new ArrayList<>();
}
