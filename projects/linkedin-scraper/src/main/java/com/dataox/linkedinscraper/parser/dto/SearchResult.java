package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"collectedAt"})
@NoArgsConstructor
public class SearchResult {

    private Long id;

    private Instant collectedAt;

    private String title;

//    @NotNull
    private String url;

    private String description;

    LinkedinProfile linkedinProfile;
}
