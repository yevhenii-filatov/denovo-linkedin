package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinAccomplishmentType{
    PUBLICATIONS("Publications"),
    LANGUAGES("Languages"),
    PATENTS("Patents"),
    PROJECTS("Projects"),
    HONORS_AND_AWARDS("Honors & Awards"),
    TEST_SCORES("Test scores"),
    ORGANIZATIONS("Organizations");

    private final String type;

    LinkedinAccomplishmentType(String type) {
        this.type = type;
    }
}
