package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.types.LinkedinActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkedinActivity {

    private Instant updatedAt;

    @NotNull
    LinkedinActivityType linkedinActivityType;

    @NotNull
    private LinkedinProfile linkedinProfile;

    private List<LinkedinPost> linkedinPosts = new ArrayList<>();
}
