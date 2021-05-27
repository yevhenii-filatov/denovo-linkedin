package com.dataox.linkedinscraper.service.validator;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public enum ValidatorMessage {
    SCRAPED_HEADER("Header Section Source"),
    SCRAPED_PHOTO("Profile Photo Url"),
    SCRAPED_ABOUT("About Section Source"),
    SCRAPED_EXPERIENCE("Experiences Source"),
    SCRAPED_EDUCATION("Educations Source"),
    SCRAPED_LICENSE("License Source"),
    SCRAPED_VOLUNTEERS("Volunteers Source"),
    SCRAPED_ALL_SKILLS("All Skills Source"),
    SCRAPED_SKILLS("Skills With Endorsements Sources"),
    SCRAPED_RECOMMENDATIONS("Recommendations Sources"),
    SCRAPED_ACCOMPLISHMENTS("Accomplishments Sources"),
    SCRAPED_INTERESTS("Interests Sources"),
    SCRAPED_ACTIVITIES("Activities Sources"),
    PARSER_ACTIVITIES("Activities"),
    PARSER_INFO("Basic Profile Info"),
    PARSER_EDUCATION("Educations"),
    PARSER_SKILLS("Skills"),
    PARSER_EXPERIENCE("Experiences"),
    PARSER_INTERESTS("Interests"),
    PARSER_LICENSE("License Certifications"),
    PARSER_RECOMMENDATIONS("Recommendations"),
    PARSER_VOLUNTEERS("VolunteerExperiences"),
    PARSER_ACCOMPLISHMENTS("Accomplishments");

    ValidatorMessage(String message) {
        this.message = message;
    }

    String message;

    String getMessage() {
        return String.format("\"%s\"", this.message);
    }
}
