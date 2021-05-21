package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinActivity {

//    @NotNull
    private Instant updatedAt;

//    @NotNull
    private LinkedinActivityType linkedinActivityType;

//    @NotNull
    private LinkedinPost linkedinPost;
}
