package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinRecommendationType {
    RECEIVED("Received"),
    GIVEN("Given");

    private final String type;

    LinkedinRecommendationType(String type) {
        this.type = type;
    }
}
