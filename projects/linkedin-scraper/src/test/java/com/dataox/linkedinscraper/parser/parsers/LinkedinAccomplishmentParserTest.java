package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinAccomplishment;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinAccomplishmentParserTest {
    private static final String ACCOMPLISHMENT_SOURCE = "/sources/linkedin-accomplishment-parser/accomplishment-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(ACCOMPLISHMENT_SOURCE);

        LinkedinParser<List<LinkedinAccomplishment>, List<String>> parser = new LinkedinAccomplishmentParser();
        List<LinkedinAccomplishment> parse = parser.parse(List.of(source));
        System.out.println(parse.get(0).getDescription());
    }

}
