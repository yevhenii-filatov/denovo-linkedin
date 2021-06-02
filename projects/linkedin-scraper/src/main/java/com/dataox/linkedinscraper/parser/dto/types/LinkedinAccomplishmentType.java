package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinAccomplishmentType {
    PUBLICATIONS("Publications"),
    LANGUAGES("Languages"),
    PATENTS("Patents"),
    PROJECTS("Projects"),
    PROJECT("Project"),
    HONORS_AND_AWARDS("Honors & Awards"),
    HONOR_AWARD("Honor & Award"),
    TEST_SCORES("Test scores"),
    ORGANIZATIONS("Organizations"),
    COURSES("Courses");


    private final String type;

    LinkedinAccomplishmentType(String type) {
        this.type = type;
    }
}
