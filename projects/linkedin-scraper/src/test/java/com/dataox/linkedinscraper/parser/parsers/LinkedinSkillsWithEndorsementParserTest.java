package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinSkill;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinSkillsWithEndorsementParserTest {

    private static final String SKILLS_SOURCE = "/sources/linkedin-skills-parsers/skill-with-endoresement.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(SKILLS_SOURCE);

        Map<String, List<String>> mapSource = Map.of("Top Three", List.of(source));

        LinkedinParser<List<LinkedinSkill>, Map<String, List<String>>> parser = new LinkedinSkillsWithEndorsementParser(new LinkedinEndorsementParser());

        List<LinkedinSkill> parse = parser.parse(mapSource);
        LinkedinSkill linkedinSkill = parse.get(0);
        System.out.println(linkedinSkill.getNumberOfEndorsements());
        System.out.println(linkedinSkill.getName());
        System.out.println(linkedinSkill.getCategory());
        linkedinSkill.getLinkedinEndorsements().forEach(linkedinEndorsement -> System.out.println(linkedinEndorsement.getEndorserFullName()));
    }

}
