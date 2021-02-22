package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
@RequiredArgsConstructor
public class LinkedinPostParser implements LinkedinParser<LinkedinPost, Map<String, Element>> {

    private final TimeConverter timeConverter;
    private final LinkedinCommentParser commentParser;

    @Override
    public LinkedinPost parse(Map<String, Element> source) {
        if (source.isEmpty()) {
            return null;
        }

        Instant time = Instant.now();

        //noinspection OptionalGetWithoutIsPresent
        return source.entrySet().stream()
                .map(entry -> {
                    String postUrl = entry.getKey();
                    Element postElement = entry.getValue();
                    LinkedinPost linkedinPost = getLinkedinPost(postElement, time, postUrl);

                    if (isCommentsPresent(postElement)) {
                        setComments(postElement, linkedinPost);
                    }

                    return linkedinPost;
                })
                .findAny()
                .get();
    }

    public LinkedinPost getLinkedinPost(Element postElement, Instant time, String postUrl) {
        LinkedinPost post = new LinkedinPost();

        post.setCollectedDate(time);
        post.setItemSource(postElement.html());
        post.setUrl(postUrl);
        post.setRelativePublicationDate(parseRelativePublicationDate(postElement));
        post.setAbsolutePublicationDate(getAbsolutePublicationDate(postElement));
        post.setAuthorProfileUrl(parseAuthorProfileUrl(postElement));
        post.setAuthorConnectionDegree(parseAuthorConnectionDegree(postElement));
        post.setAuthorHeadline(parseAuthorHeadline(postElement));
        post.setContent(parseContent(postElement));
        post.setNumberOfComments(parseNumberOfComments(postElement));
        post.setNumberOfReactions(parseNumberOfReactions(postElement));

        return post;
    }

    private void setComments(Element postElement, LinkedinPost linkedinPost) {
        Element commentElement = postElement.selectFirst(".feed-shared-update-v2__comments-container");
        List<LinkedinComment> comments = commentParser.parse(commentElement.html());
        linkedinPost.setLinkedinComments(comments);
    }

    private boolean isCommentsPresent(Element postElement) {
        return nonNull(postElement.selectFirst(".feed-shared-update-v2__comments-container"));
    }

    private String parseRelativePublicationDate(Element postElement) {
        return substringBefore(text(postElement.selectFirst("span.feed-shared-actor__sub-description")), " •");
    }

    private Instant getAbsolutePublicationDate(Element postElement) {
        return timeConverter.getAbsoluteTime(parseRelativePublicationDate(postElement));
    }

    private String parseAuthorProfileUrl(Element postElement) {
        return absUrlFromHref(postElement.selectFirst("a[data-control-name=original_share_actor_container]"));
    }

    private String parseAuthorConnectionDegree(Element postElement) {
        String selector = ".feed-shared-actor__supplementary-actor-info";
        String connectionDegree = isShared(postElement)
                ? text(postElement.select(selector).last())
                : text(postElement.selectFirst(selector));

        return substringAfter(connectionDegree, "•");
    }

    private String parseAuthorHeadline(Element postElement) {
        String selector = ".feed-shared-actor__description";
        return isShared(postElement)
                ? text(postElement.select(selector).last())
                : text(postElement.selectFirst(selector));
    }

    private String parseContent(Element postElement) {
        String selector = ".feed-shared-text";
        return isShared(postElement)
                ? text(postElement.select(selector).last())
                : text(postElement.selectFirst(selector));
    }

    private Boolean isShared(Element postElement) {
        return postElement.select(".feed-shared-actor__title").size() > 1;
    }

    private int parseNumberOfComments(Element postElement) {
        String numberOfComments = text(postElement.selectFirst("li.social-details-social-counts__comments"));
        return nonNull(numberOfComments)
                ? parseInt(substringBefore(numberOfComments, "comments").trim())
                : 0;
    }

    private int parseNumberOfReactions(Element postElement) {
        String numberOfReactions = text(postElement.selectFirst("span.social-details-social-counts__reactions-count"));
        return nonNull(numberOfReactions)
                ? parseInt(numberOfReactions)
                : 0;
    }
}
