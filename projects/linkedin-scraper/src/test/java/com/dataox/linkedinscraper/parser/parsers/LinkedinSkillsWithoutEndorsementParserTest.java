package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinSkillsWithoutEndorsementParserTest {

    private static final String SKILLS_SOURCE = "/sources/linkedin-skills-parsers/skills-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(SKILLS_SOURCE);

        LinkedinParser<List<LinkedinSkill>, String> parser = new LinkedinSkillsWithoutEndorsementParser();

        List<LinkedinSkill> skills = parser.parse(source);

        assertThat(skills.size()).isEqualTo(14);

        LinkedinSkill actual = skills.get(12);

        LinkedinSkill expected = new LinkedinSkill();
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setCategory("Other Skills");
        expected.setName("TypeScript");
        expected.setItemSource("md5:7973b089c7fd4ca3baf371ed8f946b58");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual skills source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}
