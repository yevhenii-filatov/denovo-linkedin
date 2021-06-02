package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class LinkedinComment {

    @ToString.Include
    private String url;

//    @NotBlank
    private String itemSource;

//    @NotBlank
@EqualsAndHashCode.Include
@ToString.Include
    private String content;

//    @NotBlank
//    @Max(20)
    private String relativePublicationDate;

    @ToString.Include
//    @NotNull
    private Instant collectedDate;

    @ToString.Include
//    @NotNull
    private Instant absolutePublicationDate;

//    @EqualsAndHashCode.Include
    private int numberOfReactions;

//    @EqualsAndHashCode.Include
    private int numberOfReplies;
}
