package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinRecommendation;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinRecommendationParserTest {

    private static final String RECOMMENDATION_SOURCE = "/sources/linkedin-recommendation-parser/recommendation-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(RECOMMENDATION_SOURCE);
        RecommendationsSource typeSource = new RecommendationsSource("Given", source);
        LinkedinParser<List<LinkedinRecommendation>, List<RecommendationsSource>> parser = new LinkedinRecommendationParser();

        List<LinkedinRecommendation> recommendations = parser.parse(Collections.singletonList(typeSource));

        assertThat(recommendations.size()).isEqualTo(3);

        LinkedinRecommendation actual = recommendations.get(1);

        LinkedinRecommendation expected = new LinkedinRecommendation();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setType("Given");
        expected.setPersonFullName("Dmitry Stril");
        expected.setPersonProfileUrl("https://www.linkedin.com/in/dmitrystril/");
        expected.setPersonHeadline("Software Developer");
        expected.setPersonExtraInfo("May 6, 2019, Alexander was senior to Dmitry but didn’t manage directly");
        expected.setDescription(
                "I have worked with Dmitry more than 2 years and can say he is very responsible, " +
                "honest and clever. His code is high quality and he saw any obstacles in advance. Besides working on " +
                "projects he talked to our clients directly and they were excited to work with him. " +
                "Very recommend to work with Dmitry and if you want to know more information please contact me."
        );
        expected.setItemSource("md5:d116d0bddecf386a86a8bf3a90a08f09");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual certification source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}
