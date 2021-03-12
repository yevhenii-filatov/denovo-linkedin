package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.linkedinscraper.parser.ParsingTestUtils.loadResource;

class LinkedinActivityParserTest {
    private static final String ACTIVITY_SOURCE = "/sources/linkedin-activity-parser/activity-feed.html";

    @Test
    void shouldParse() throws IOException {
        String source = loadResource(ACTIVITY_SOURCE);

        LinkedinPostParser postParser = new LinkedinPostParser(new TimeConverter()
                ,new LinkedinCommentParser(new TimeConverter()));

        List<String> activitySource = Jsoup.parse(source).select(".occludable-update.ember-view").stream()
                .map(Element::html)
                .collect(Collectors.toList());

        LinkedinParser<List<LinkedinActivity>,List<String>> parser1 = new LinkedinActivityParser(postParser);
        List<LinkedinActivity> activities = parser1.parse(activitySource);


        System.out.println(activities.get(2).getLinkedinPost().getContent());;


    }

}
