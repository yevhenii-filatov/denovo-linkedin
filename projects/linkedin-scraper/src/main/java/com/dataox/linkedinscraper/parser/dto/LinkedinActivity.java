package com.dataox.linkedinscraper.parser.dto;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Data
//@EqualsAndHashCode(exclude = {"updatedAt"})
@NoArgsConstructor
public class LinkedinActivity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinActivity that = (LinkedinActivity) o;
        return linkedinActivityType == that.linkedinActivityType && Objects.equals(linkedinPost, that.linkedinPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updatedAt, linkedinActivityType, linkedinPost);
    }

    //    @NotNull
    private Instant updatedAt;

//    @NotNull
    private LinkedinActivityType linkedinActivityType;

//    @NotNull
    private LinkedinPost linkedinPost;
}
