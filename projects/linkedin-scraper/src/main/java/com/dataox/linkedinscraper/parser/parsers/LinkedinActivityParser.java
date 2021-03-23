package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinActivity;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.service.mappers.LinkedinActivityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinActivityParser implements LinkedinParser<List<LinkedinActivity>, List<String>> {

    private final LinkedinPostParser postParser;
    private final LinkedinActivityMapper mapper;

    @Override
    public List<LinkedinActivity> parse(List<String> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        List<LinkedinActivity> activities = source.stream()
                .map(activitySource -> getLinkedinActivity(time, activitySource))
                .collect(Collectors.toList());


        //makePostsUniqAndCombineComments(activities);

        return activities;
    }

    private LinkedinActivity getLinkedinActivity(Instant time, String source) {
        LinkedinActivity activity = new LinkedinActivity();
        Element activityElement = toElement(source);

        activity.setUpdatedAt(time);
        String type = parseType(activityElement);
        activity.setLinkedinActivityType(mapper.map(type));
        activity.setLinkedinPost(postParser.parse(source));

        return activity;
    }

    private void makePostsUniqAndCombineComments(List<LinkedinActivity> activities) {
        for (int i = 0; i < activities.size(); i++) {
            LinkedinPost post = activities.get(i).getLinkedinPost();
            for (int j = i + 1; j < activities.size(); j++) {
                LinkedinActivity activity = activities.get(j);
                if (post.equals(activity.getLinkedinPost())) {
                    Set<LinkedinComment> linkedinComments = activities.remove(j).getLinkedinPost().getLinkedinComments();
                    post.getLinkedinComments().addAll(linkedinComments);
                    --j;
                }
            }
        }
    }

    private String parseType(Element activityElement) {
        String activityType = text(activityElement.selectFirst("span.feed-shared-header__text-view button + span"));
        return nonNull(activityType)
                ? activityType
                : "shared this";
    }
}
