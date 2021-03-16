package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinCommentParserTest {

    private static final String COMMENT_SOURCE = "/sources/linkedin-comment-parser/comment-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(COMMENT_SOURCE);
        LinkedinParser<Set<LinkedinComment>, String> parser = new LinkedinCommentParser(new TimeConverter());

        Set<LinkedinComment> parse = parser.parse(source);

        parse.forEach(e -> System.out.println(e.getUrl() + "  "  + "\n"));
    }
}
