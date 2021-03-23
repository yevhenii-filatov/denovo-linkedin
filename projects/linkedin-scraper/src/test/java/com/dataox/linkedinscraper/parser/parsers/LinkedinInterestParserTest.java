package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinInterest;
import com.dataox.linkedinscraper.parser.utils.sources.InterestsSource;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinInterestParserTest {

    private static final String COMPANIES_SOURCE = "/sources/linkedin-interests-parser/companies-interests-section.html";

    @Test
    void shouldParse() throws IOException {
        InterestsSource source = new InterestsSource("Companies", loadResource(COMPANIES_SOURCE));
        LinkedinParser<List<LinkedinInterest>, List<InterestsSource> > parser = new LinkedinInterestParser();

        List<LinkedinInterest> interests = parser.parse(Collections.singletonList(source));

        assertThat(interests.size()).isEqualTo(11);

        LinkedinInterest actual = interests.get(0);

        LinkedinInterest expected = new LinkedinInterest();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setName("Sama");
        expected.setType("COMPANIES");
        expected.setProfileUrl("https://www.linkedin.com/company/sama-ai/");
        expected.setNumberOfFollowers("31258");
        expected.setHeadline("");
        expected.setItemSource("md5:98cc31993eab6c6ee193ae4962bc20d7");


        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual interests source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}
