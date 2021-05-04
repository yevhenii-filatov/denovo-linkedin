package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.service.TimeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.hashedStingComparator;
import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;
import static org.assertj.core.api.Assertions.assertThat;

class LinkedinCommentParserTest {

    private static final String COMMENT_SOURCE = "/sources/linkedin-comment-parser/comment-section.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(COMMENT_SOURCE);
        LinkedinParser<Set<LinkedinComment>, String> parser = new LinkedinCommentParser(new TimeConverter());

        Set<LinkedinComment> comments = parser.parse(source);
        List<LinkedinComment> commentList = new ArrayList<>(comments);

        assertThat(commentList.size()).isEqualTo(14);

        LinkedinComment actual = commentList.get(0);

        LinkedinComment expected = new LinkedinComment();
        expected.setUrl("https://www.linkedin.com/feed/update/urn:li:activity:6765026591211839488?commentUrn=urn%3Ali%3Acomment%3A%28activity%3A6765026591211839488%2C6765201624974675968%29");
        expected.setNumberOfReactions(6);
        expected.setNumberOfReplies(1);
        expected.setCollectedDate(actual.getCollectedDate());
        expected.setContent("Brilliant.");
        expected.setRelativePublicationDate("7h");
        expected.setAbsolutePublicationDate(actual.getAbsolutePublicationDate());
        expected.setItemSource("md5:2de6d574743f0ad8db96d870ee792378");


        assertThat(actual)
                .as("all except sources")
                .isEqualToIgnoringGivenFields(expected, "itemSource");

        assertThat(actual)
                .as("actual comment source hash md5:" + DigestUtils.md5DigestAsHex(actual.getItemSource().getBytes()))
                .usingComparatorForFields(hashedStingComparator(), "itemSource")
                .isEqualToComparingOnlyGivenFields(expected, "itemSource");
    }
}

