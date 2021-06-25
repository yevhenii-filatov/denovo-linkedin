package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.dto.sources.SkillEndorsementsSource;
import com.dataox.linkedinscraper.dto.sources.SkillsSource;
import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinSkillsWithEndorsementParserTest {

    private static final String SKILLS_SOURCE = "/sources/linkedin-skills-parsers/skill-with-endoresement.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(SKILLS_SOURCE);

        SkillsSource skillsSource = new SkillsSource("Top Three",
                List.of(new SkillEndorsementsSource("https://www.linkedin.com/in/alexander-demchenko/detail/skills/(ACoAABW5p6YBmK0zd_9J3zxe16T5alPKjFkskQE,2)/", source)));

        LinkedinParser<List<LinkedinSkill>, List<SkillsSource>> parser = new LinkedinSkillsWithEndorsementParser(new LinkedinEndorsementParser());

        List<LinkedinSkill> parse = parser.parse(Collections.singletonList(skillsSource));
        LinkedinSkill actual = parse.get(0);

        LinkedinSkill expected = new LinkedinSkill();
        expected.setCategory("Top Three");
        expected.setUrl("https://www.linkedin.com/in/alexander-demchenko/detail/skills/(ACoAABW5p6YBmK0zd_9J3zxe16T5alPKjFkskQE,2)/");
        expected.setName("Web Analytics");
        expected.setNumberOfEndorsements(9);
        expected.setLinkedinEndorsements(actual.getLinkedinEndorsements());
        assertThat(expected.getLinkedinEndorsements().size()).isEqualTo(expected.getNumberOfEndorsements());
        expected.setUpdatedAt(actual.getUpdatedAt());
        expected.setItemSource("md5:6bc1f23fd620c79e074d963d5d09e059");

        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual skills with endorsement source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");

    }

}
