package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import com.dataox.linkedinscraper.parser.utils.sources.ActivitiesSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinPostParserTest {
    private static final String ACTIVITY_SOURCE = "/sources/linkedin-activity-parser/activity-source.html";
    private static final String POST_URL = "https://www.linkedin.com/posts/elizabeth-derbes-35471518_proud-to-have-led-the-good-food-institutes-activity-6767175247679512576-Okgq";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(ACTIVITY_SOURCE);
        LinkedinPostParser parser = new LinkedinPostParser(new TimeConverter()
        ,new LinkedinCommentParser(new TimeConverter()));

        LinkedinPost post = parser.parse(source);

        System.out.println(post.getLinkedinComments().size());

        ActivitiesSource source1 = new ActivitiesSource(POST_URL, loadResource(ACTIVITY_SOURCE));
        LinkedinParser<List<LinkedinActivity>,List<String>> parser1 = new LinkedinActivityParser(parser);
        List<LinkedinActivity> parse = parser1.parse(List.of(source));
        System.out.println(parse.get(0).getLinkedinPost().getContent());

    }
}
