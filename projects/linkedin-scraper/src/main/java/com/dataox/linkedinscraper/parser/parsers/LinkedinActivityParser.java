package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.utils.sources.ActivitiesSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinActivityParser implements LinkedinParser<List<LinkedinActivity>, List<String>> {

    private final LinkedinPostParser postParser;

    @Override
    public List<LinkedinActivity> parse(List<String> source) {
        if (source.isEmpty()) {
            log.info("{} received empty source", this.getClass().getSimpleName());
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .map(activitySource -> getLinkedinActivity(time,activitySource))
                .collect(Collectors.toList());
    }

    private LinkedinActivity getLinkedinActivity(Instant time, String source) {
        LinkedinActivity activity = new LinkedinActivity();
        Element activityElement = toElement(source);

        activity.setUpdatedAt(time);
        activity.setType(parseType(activityElement));
        activity.setLinkedinPost(postParser.parse(source));

        return activity;
    }

    private String parseType(Element activityElement) {
        String activityType = text(activityElement.selectFirst("span.feed-shared-header__text-view button + span"));
        return nonNull(activityType)
                ? activityType
                : "shared this";
    }
}
