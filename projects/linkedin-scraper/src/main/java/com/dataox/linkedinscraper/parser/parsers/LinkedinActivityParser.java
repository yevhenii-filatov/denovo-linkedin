package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.utils.sources.ActivitiesSource;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LinkedinActivityParser implements LinkedinParser<List<LinkedinActivity>, List<ActivitiesSource>> {

    private final LinkedinPostParser postParser;

    @Override
    public List<LinkedinActivity> parse(List<ActivitiesSource> source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .map(activitySource -> getLinkedinActivity(time,activitySource))
                .collect(Collectors.toList());
    }

    private LinkedinActivity getLinkedinActivity(Instant time, ActivitiesSource source) {
        LinkedinActivity activity = new LinkedinActivity();
        Element activityElement = toElement(source.getSource());

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
