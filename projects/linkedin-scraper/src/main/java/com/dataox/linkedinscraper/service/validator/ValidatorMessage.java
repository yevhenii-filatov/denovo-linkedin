package com.dataox.linkedinscraper.service.validator;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public enum ValidatorMessage {
    SCRAPED_HEADER("scraped Header Section Source"),
    SCRAPED_PHOTO("scraped Profile Photo Url"),
    SCRAPED_ABOUT("scraped About Section Source"),
    SCRAPED_EXPERIENCE("scraped Experiences Source"),
    SCRAPED_EDUCATION("scraped Educations Source"),
    SCRAPED_LICENSE("scraped License Source"),
    SCRAPED_VOLUNTEERS("scraped Volunteers Source"),
    SCRAPED_ALL_SKILLS("scraped All Skills Source"),
    SCRAPED_SKILLS("scraped Skills With Endorsements Sources"),
    SCRAPED_RECOMMENDATIONS("scraped Recommendations Sources"),
    SCRAPED_ACCOMPLISHMENTS("scraped Accomplishments Sources"),
    SCRAPED_INTERESTS("scraped Interests Sources"),
    SCRAPED_ACTIVITIES("scraped Activities Sources"),
    PARSER_ACTIVITIES("parsed Activities"),
    PARSER_INFO("parsed Basic Profile Info"),
    PARSER_EDUCATION("parsed Educations"),
    PARSER_SKILLS("parsed Skills"),
    PARSER_EXPERIENCE("parsed Experiences"),
    PARSER_INTERESTS("parsed Interests"),
    PARSER_LICENSE("parsed License Certifications"),
    PARSER_RECOMMENDATIONS("parsed Recommendations"),
    PARSER_VOLUNTEERS("parsed VolunteerExperiences"),
    PARSER_ACCOMPLISHMENTS("parsed Accomplishments");

    ValidatorMessage(String message) {
        this.message = message;
    }

    String message;

    String getMessage() {
        return String.format("\"%s\"", this.message);
    }
}
