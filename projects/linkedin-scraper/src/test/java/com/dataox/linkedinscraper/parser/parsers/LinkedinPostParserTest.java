package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinPostParserTest {
    private static final String ACTIVITY_SOURCE = "/sources/linkedin-activity-parser/activity-source.html";
    private static final String POST_URL = "https://www.linkedin.com/posts/elizabeth-derbes-35471518_proud-to-have-led-the-good-food-institutes-activity-6767175247679512576-Okgq";

    @Test
    void shouldParse() throws IOException {
        Map<String, Element> source = Map.of(POST_URL, Jsoup.parse(loadResource(ACTIVITY_SOURCE)).body());
        LinkedinPostParser parser = new LinkedinPostParser(new TimeConverter()
        ,new LinkedinCommentParser(new TimeConverter()));

        LinkedinPost post = parser.parse(source);

        System.out.println(post.getLinkedinComments().size());

        Map<String, String> source1 = Map.of(POST_URL, loadResource(ACTIVITY_SOURCE));
        LinkedinParser<List<LinkedinActivity>,Map<String, String>> parser1 = new LinkedinActivityParser(parser);
        List<LinkedinActivity> parse = parser1.parse(source1);
        System.out.println(parse.get(0).getLinkedinPost().getUrl());

    }
}
