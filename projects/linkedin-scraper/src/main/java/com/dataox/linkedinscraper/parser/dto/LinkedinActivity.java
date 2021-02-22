package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinActivity {

    @NotNull
    private Instant updatedAt;

    @NotNull
    private String type;

    @NotNull
    private LinkedinPost linkedinPost;
}
