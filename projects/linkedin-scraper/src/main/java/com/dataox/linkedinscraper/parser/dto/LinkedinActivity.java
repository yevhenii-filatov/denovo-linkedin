package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinActivityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt"})
@ToString(exclude = {"updatedAt"})
@NoArgsConstructor
public class LinkedinActivity {

    //    @NotNull
    private Instant updatedAt;

    //    @NotNull
    private LinkedinActivityType linkedinActivityType;

    //    @NotNull
    private LinkedinPost linkedinPost;
}
