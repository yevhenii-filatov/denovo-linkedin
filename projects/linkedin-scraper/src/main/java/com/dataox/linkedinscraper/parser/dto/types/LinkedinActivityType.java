package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinActivityType {
    LIKED_THIS("likes this"),
    COMMENTED_ON("commented on this"),
    SHARED_THIS("shared this"),
    REPLIED_TO_COMMENT("replied to"),
    JOB_UPDATE("job update"),
    CUSTOM_POST("a"),
    LIKED_COMMENT("liked");

    private final String type;

    LinkedinActivityType(String type) {
        this.type = type;
    }
}
