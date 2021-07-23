package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class LinkedinPost {

    private String url;

    //    @NotBlank
    private String itemSource;

    //    @NotBlank
//    @Max(20)
    private String relativePublicationDate;

    //    @NotNull
    private Instant collectedDate;

    //    @NotNull
    private Instant absolutePublicationDate;

    @EqualsAndHashCode.Include
    @ToString.Include
//    @NotBlank
    private String authorProfileName;

    @EqualsAndHashCode.Include
    @ToString.Include
//    @NotBlank
    private String authorProfileUrl;

    //    @NotBlank
    private String authorConnectionDegree;

    //    @NotBlank
    private String authorHeadline;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String content;

    private int numberOfComments;

    private int numberOfReactions;

    private Set<LinkedinComment> linkedinComments = new HashSet<>();
}
