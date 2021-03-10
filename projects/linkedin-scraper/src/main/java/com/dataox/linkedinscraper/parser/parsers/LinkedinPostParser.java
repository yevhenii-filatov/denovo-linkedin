package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.exceptions.EmptySourceException;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinPostParser implements LinkedinParser<LinkedinPost, String> {
    private final static String BASE_URL = "https://www.linkedin.com/feed/update/";

    private final TimeConverter timeConverter;
    private final LinkedinCommentParser commentParser;

    @Override
    public LinkedinPost parse(String source) {
        if (source.isEmpty()) {
            log.error("received empty source", new EmptySourceException("Post parser shouldn't receive empty source"));
            return null;
        }

        Instant time = Instant.now();

        Element postElement = toElement(source);

        return getLinkedinPost(time, postElement);
    }

    public LinkedinPost getLinkedinPost(Instant time, Element postElement) {
        LinkedinPost post = new LinkedinPost();

        post.setCollectedDate(time);
        post.setItemSource(postElement.html());
        post.setUrl(getActivityUrl(postElement));

        String relativePublicationDate = parseRelativePublicationDate(postElement);
        if (isNotBlank(relativePublicationDate)) {
            post.setRelativePublicationDate(relativePublicationDate);
            post.setAbsolutePublicationDate(getAbsolutePublicationDate(post.getRelativePublicationDate()));
        }

        post.setAuthorProfileUrl(parseAuthorProfileUrl(postElement));
        post.setAuthorConnectionDegree(parseAuthorConnectionDegree(postElement));
        post.setAuthorHeadline(parseAuthorHeadline(postElement));
        post.setContent(parseContent(postElement));
        post.setNumberOfComments(parseNumberOfComments(postElement));
        post.setNumberOfReactions(parseNumberOfReactions(postElement));

        if (isCommentsPresent(postElement)) {
            setComments(postElement, post);
        }

        return post;
    }

    private String getActivityUrl(Element postElement) {
        return BASE_URL + parsePostUrn(postElement);
    }

    private String parsePostUrn(Element postElement) {
        return postElement.selectFirst("div[data-urn]").attr("data-urn");
    }

    private String parseRelativePublicationDate(Element postElement) {
        String baseSelector = "span.feed-shared-actor__sub-description";
        String date = text(postElement.selectFirst(baseSelector + "> span > span:lt(1)"));

        return nonNull(date)
                ? substringBefore(date, " •")
                : substringBefore(text(postElement.selectFirst(baseSelector + "> span:lt(1)")), " •") ;
    }

    private Instant getAbsolutePublicationDate(String relativePublicationDate) {
        return timeConverter.getAbsoluteTime(relativePublicationDate);
    }

    private String parseAuthorProfileUrl(Element postElement) {
       return isShared(postElement)
                ? absUrlFromHref(postElement.selectFirst("a[data-control-name=original_share_actor_container]"))
                : absUrlFromHref(postElement.selectFirst("a[data-control-name=actor_container]"));
        //return absUrlFromHref(postElement.selectFirst(selector));
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
        String content = isShared(postElement)
                ? text(postElement.select(selector).last())
                : text(postElement.selectFirst(selector));

        return handleArticle(postElement, content);
    }

    private String handleArticle(Element postElement, String content) {
        return nonNull(content)
                ? content
                : text(postElement.select(".feed-shared-article__section,.feed-shared-article__description"));
    }

    private Boolean isShared(Element postElement) {
        return postElement.select(".feed-shared-actor__title").size() > 1;
    }

    private int parseNumberOfComments(Element postElement) {
        String numberOfComments = text(postElement.selectFirst("li.social-details-social-counts__comments"));
        return nonNull(numberOfComments)
                ? parseInt(numberOfComments.replaceAll("\\D+", ""))
                : 0;
    }

    private int parseNumberOfReactions(Element postElement) {
        String numberOfReactions = text(postElement.selectFirst("span.social-details-social-counts__reactions-count"));
        return nonNull(numberOfReactions)
                ? parseInt(numberOfReactions.replaceAll("\\D+", ""))
                : 0;
    }

    private void setComments(Element postElement, LinkedinPost linkedinPost) {
        Element commentElement = postElement.selectFirst(".feed-shared-update-v2__comments-container");
        Set<LinkedinComment> comments = commentParser.parse(commentElement.html());
        linkedinPost.setLinkedinComments((comments));
    }

    private boolean isCommentsPresent(Element postElement) {
        return nonNull(postElement.selectFirst(".feed-shared-update-v2__comments-container"));
    }
}
