package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinVolunteerExperience;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinVolunteerExperienceParserTest {

    private static final String EXPERIENCE_SOURCE = "/sources/linkedin-volunteer-experience-parser/volunteer-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(EXPERIENCE_SOURCE);
        LinkedinParser<List<LinkedinVolunteerExperience>, String> parser = new LinkedinVolunteerExperienceParser();

        List<LinkedinVolunteerExperience> volunteerExperiences = parser.parse(source);

        assertThat(volunteerExperiences.size()).isEqualTo(4);

        LinkedinVolunteerExperience actual = volunteerExperiences.get(2);

        LinkedinVolunteerExperience expected = new LinkedinVolunteerExperience();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setCompanyName("Girl Scouts of the USA");
        expected.setCompanyProfileUrl("https://www.linkedin.com/company/girl-scouts-of-the-usa/");
        expected.setPosition("Cookie MoMster");
        expected.setDateStarted("Jan 2007");
        expected.setDateFinished("Aug 2014");
        expected.setTotalDuration("7 yrs 8 mos");
        expected.setFieldOfActivity("Children");
        expected.setDescription("Product Sales Management - Cookies; Nuts, Magazines");
        expected.setItemSource("md5:ae6c2d92293e53d33d8891237a352533");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual volunteer source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}
