package com.dataox.linkedinscraper.parser.dto.types;

import lombok.Getter;

@Getter
public enum LinkedinAccomplishmentType {
    PUBLICATIONS("Publications"),
    PUBLICATION("Publication"),
    LANGUAGES("Languages"),
    LANGUAGE("Language"),
    PATENTS("Patents"),
    PATENT("Patent"),
    PROJECTS("Projects"),
    PROJECT("Project"),
    HONORS_AND_AWARDS("Honors & Awards"),
    HONOR_AWARD("Honor & Award"),
    TEST_SCORES("Test scores"),
    TEST_SCORE("Test score"),
    ORGANIZATIONS("Organizations"),
    ORGANIZATION("Organization"),
    COURSES("Courses"),
    COURSE("Course");

    private final String type;

    LinkedinAccomplishmentType(String type) {
        this.type = type;
    }
}
