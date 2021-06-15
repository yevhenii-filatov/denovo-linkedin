package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinBasicProfileInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinBasicProfileInfoParserTest {

    private static final String ABOUT_SOURCE = "/sources/linkedin-basic-profile-parser/about-section.html";
    private static final String HEADER_SOURCE = "/sources/linkedin-basic-profile-parser/header-section.html";
    private List<String> combinedSource;

    @BeforeEach
    void setUp() throws IOException {
        combinedSource = List.of(loadResource(HEADER_SOURCE),loadResource(ABOUT_SOURCE));
    }

    @Test
    void shouldParse(){
        LinkedinParser<LinkedinBasicProfileInfo,List<String>> parser = new LinkedinBasicProfileInfoParser();
        LinkedinBasicProfileInfo actual = parser.parse(combinedSource);

        LinkedinBasicProfileInfo expected = new LinkedinBasicProfileInfo();
        expected.setFullName("Alexander Demchenko");
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setNumberOfConnections("500+");
        expected.setLocation("Ukraine");
        expected.setAbout(
                "I am a co-founder of a software company named DataOx engaged in web scraping services and data cleansing technologies. " +
                "We are a company focused on providing high-quality data scraping and data processing solutions for enterprises and individuals. " +
                "We have 6+ years of experience and more than 200 projects successfully done. " +
                "Our team of young, innovative, and talented guys have developed an internal intellectual platform to do scraping projects faster " +
                "and in a cost-effective way. Ask our clients about us! My goal is to apply our expertise as much as possible! " +
                "Contact me at: mobile: +380 (98) 159 07 22 skype: aldemind e-mail: alex@dataox.io website: dataox.io"
        );

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "headerSectionSource","aboutSectionSource");

        assertThat(actual).isEqualTo(expected);
    }
}
