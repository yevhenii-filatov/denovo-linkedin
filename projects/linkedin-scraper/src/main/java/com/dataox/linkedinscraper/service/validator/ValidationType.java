package com.dataox.linkedinscraper.service.validator;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public enum ValidationType {
    SCRAPED_HEADER("Scraped Header Section Source"),
    SCRAPED_PHOTO("Scraped Profile Photo Url"),
    SCRAPED_ABOUT("Scraped About Section Source"),
    SCRAPED_EXPERIENCE("Scraped Experiences Source"),
    SCRAPED_EDUCATION("Scraped Educations Source"),
    SCRAPED_LICENSE("Scraped License Source"),
    SCRAPED_VOLUNTEERS("Scraped Volunteers Source"),
    SCRAPED_ALL_SKILLS("Scraped All Skills Source"),
    SCRAPED_SKILLS("Scraped Skills With Endorsements Sources"),
    SCRAPED_RECOMMENDATIONS("Scraped Recommendations Sources"),
    SCRAPED_ACCOMPLISHMENTS("Scraped Accomplishments Sources"),
    SCRAPED_INTERESTS("Scraped Interests Sources"),
    SCRAPED_ACTIVITIES("Scraped Activities Sources"),
    PARSER_ACTIVITIES("Parsed Activities"),
    PARSER_INFO("Parsed Basic Profile Info"),
    PARSER_EDUCATION("Parsed Educations"),
    PARSER_SKILLS("Parsed Skills"),
    PARSER_EXPERIENCE("Parsed Experiences"),
    PARSER_INTERESTS("Parsed Interests"),
    PARSER_LICENSE("Parsed License Certifications"),
    PARSER_RECOMMENDATIONS("Parsed Recommendations"),
    PARSER_VOLUNTEERS("Parsed Volunteer Experiences"),
    PARSER_ACCOMPLISHMENTS("Parsed Accomplishments");

    ValidationType(String value) {
        this.value = value;
    }

    private final String value;

    String getMessage() {
        return this.value;
    }
}
