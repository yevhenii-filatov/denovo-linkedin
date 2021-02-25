package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import com.dataox.linkedinscraper.parser.utils.sources.SkillsSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static com.dataox.linkedinscraper.parser.utils.sources.SkillsSource.SkillEndorsementsSource;

class LinkedinSkillsWithEndorsementParserTest {

    private static final String SKILLS_SOURCE = "/sources/linkedin-skills-parsers/skill-with-endoresement.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(SKILLS_SOURCE);

        SkillsSource skillsSource = new SkillsSource("Top Three",
                List.of(new SkillEndorsementsSource("https://www.linkedin.com/in/alexander-demchenko/detail/skills/(ACoAABW5p6YBmK0zd_9J3zxe16T5alPKjFkskQE,2)/",source)));

        LinkedinParser<List<LinkedinSkill>, List<SkillsSource>> parser = new LinkedinSkillsWithEndorsementParser(new LinkedinEndorsementParser());

        List<LinkedinSkill> parse = parser.parse(Collections.singletonList(skillsSource));
        LinkedinSkill linkedinSkill = parse.get(0);
        System.out.println(linkedinSkill.getNumberOfEndorsements());
        System.out.println(linkedinSkill.getName());
        System.out.println(linkedinSkill.getCategory());
        System.out.println(linkedinSkill.getUrl());
        linkedinSkill.getLinkedinEndorsements().forEach(linkedinEndorsement -> System.out.println(linkedinEndorsement.getEndorserFullName()));
    }

}
