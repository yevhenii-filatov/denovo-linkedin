package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinRecommendationType {
    RECEIVED("RECEIVED"),
    GIVEN("GIVEN");

    private final String type;

    LinkedinRecommendationType(String type) {
        this.type = type;
    }
}
