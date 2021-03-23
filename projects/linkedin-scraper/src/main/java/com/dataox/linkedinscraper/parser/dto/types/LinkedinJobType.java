package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinJobType {
    PART_TIME("Part-time"),
    FULL_TIME("Full-time");

    private final String type;

    LinkedinJobType(String type) {
        this.type = type;
    }
}
