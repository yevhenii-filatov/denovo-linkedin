package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class LinkedinActivityParser implements LinkedinParser<List<LinkedinActivity>, Map<String, String>> {

    private final LinkedinPostParser postParser;

    @Override
    public List<LinkedinActivity> parse(Map<String, String> source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.entrySet().stream()
                .map(entry -> {
                    String postUrl = entry.getKey();
                    String activitySource = entry.getValue();

                    return getLinkedinActivity(time, toElement(activitySource), postUrl);
                })
                .collect(Collectors.toList());
    }

    private LinkedinActivity getLinkedinActivity(Instant time, Element activityElement, String postUrl) {
        LinkedinActivity activity = new LinkedinActivity();
        Map<String, Element> postUrlAndSource = Map.of(postUrl, activityElement);

        activity.setUpdatedAt(time);
        activity.setType(parseType(activityElement));
        activity.setLinkedinPost(postParser.parse(postUrlAndSource));

        return activity;
    }

    private String parseType(Element activityElement) {
        String activityType = text(activityElement.selectFirst("span.feed-shared-header__text-view button + span"));
        return nonNull(activityType)
                ? activityType
                : "shared this";
    }
}
