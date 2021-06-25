package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinExperience;
import com.dataox.linkedinscraper.parser.service.TimeConverter;
import com.dataox.linkedinscraper.parser.service.mappers.LinkedinJobTypeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinExperienceParserTest {

    private static final String EXPERIENCE_SOURCE = "/sources/linkedin-experience-parser/experience-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(EXPERIENCE_SOURCE);
        TimeConverter timeConverter = new TimeConverter();
        LinkedinParser<List<LinkedinExperience>, String> parser = new LinkedinExperienceParser(timeConverter, new LinkedinJobTypeMapper());

        List<LinkedinExperience> experiences = parser.parse(source);

        assertThat(experiences.size()).isEqualTo(4);

        LinkedinExperience actual1 = experiences.get(0);
        LinkedinExperience actual2 = experiences.get(2);

        LinkedinExperience expected1 = new LinkedinExperience();
        expected1.setCompanyName("DataOx");
        expected1.setPosition("Co-Founder");
        expected1.setCompanyProfileUrl("https://www.linkedin.com/company/dataox/");
        expected1.setUpdatedAt(actual1.getUpdatedAt());
        expected1.setDateStarted("Jan 2015");
        expected1.setDateStartedTimestamp(LocalDate.parse("2015-01-01"));
        expected1.setDateFinished("Present");
        expected1.setTotalDuration("6 yrs 2 mos");
        expected1.setLocation("Ukraine");
        expected1.setDescription("Our main focus is on providing high-quality web scraping services and data solutions.");
        expected1.setItemSource("md5:c72880488b5bb13ad4fb640b8bc6fb3a");

        LinkedinExperience expected2 = new LinkedinExperience();
        expected2.setCompanyName("IntroLab Systems");
        expected2.setPosition("Co Founder & Business analyst");
        expected2.setCompanyProfileUrl("https://www.linkedin.com/company/introlab-systems/");
        expected2.setUpdatedAt(actual2.getUpdatedAt());
        expected2.setDateStarted("Dec 2014");
        expected2.setDateFinished("Jan 2015");
        expected2.setDateStartedTimestamp(LocalDate.parse("2014-12-01"));
        expected2.setDateFinishedTimestamp(LocalDate.parse("2015-01-01"));
        expected2.setTotalDuration("2 mos");
        expected2.setLocation("Украина");
        expected2.setDescription("General management and working with data flows");
        expected2.setItemSource("md5:dab806353469c1f32dfc99facbcabc4e");

        assertThat(actual1)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected1, "itemSource");

        assertThat(actual1)
                .as("actual experience source1 hash md5:" + DigestUtils.md5DigestAsHex(actual1.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected1, "itemSource");

        assertThat(actual2)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected2, "itemSource");

        assertThat(actual2)
                .as("actual experience source2 hash md5:" + DigestUtils.md5DigestAsHex(actual2.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected2, "itemSource");
    }
}
