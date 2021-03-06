package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;


@Data
@NoArgsConstructor
public class SearchResult {

    private Long id;

    private Instant collectedAt;

    private String title;

    @NotNull
    private String url;

    private String description;

    LinkedinProfile linkedinProfile;
}
