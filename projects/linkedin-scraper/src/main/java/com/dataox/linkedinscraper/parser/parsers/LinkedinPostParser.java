package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.dto.LinkedinPost;
import com.dataox.linkedinscraper.parser.exceptions.EmptySourceException;
import com.dataox.linkedinscraper.parser.service.TimeConverter;
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
    private static final String BASE_URL = "https://www.linkedin.com/feed/update/";
    private static final String CONTENT_SELECTOR = ".feed-shared-text";
    private static final String SHARED_URL_SELECTOR = "a[data-control-name=actor_container]";
    private static final String CONNECTION_DEGREE_SELECTOR = ".feed-shared-actor__supplementary-actor-info";
    private static final String AUTHOR_HEADLINE_SELECTOR = ".feed-shared-actor__description";
    private static final String AUTHOR_NAME_SELECTOR = ".feed-shared-actor__name";

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
            if (isNotBlank(getDigits(relativePublicationDate))) {
                post.setAbsolutePublicationDate(getAbsolutePublicationDate(post.getRelativePublicationDate()));
            }
        }

        post.setAuthorProfileName(parseAuthorName(postElement));
        post.setAuthorProfileUrl(parseAuthorProfileUrl(postElement));
        post.setAuthorConnectionDegree(parseAuthorConnectionDegree(postElement));
        post.setAuthorHeadline(parseAuthorHeadline(postElement));
        post.setContent(parseContent(postElement));
        post.setNumberOfComments(parseNumberOfComments(postElement));
        post.setNumberOfReactions(parseNumberOfReactions(postElement));

        if (isCommentsPresent(postElement)) {
            setComments(postElement, post);
        }

        handleTwoPostContents(postElement, post);

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
                : substringBefore(text(postElement.selectFirst(baseSelector + "> span:lt(1)")), " •");
    }

    private Instant getAbsolutePublicationDate(String relativePublicationDate) {
        return timeConverter.getAbsoluteTime(relativePublicationDate);
    }

    private String parseAuthorName(Element postElement) {
        return isShared(postElement)
                ? text(postElement.select(AUTHOR_NAME_SELECTOR).last())
                : text(postElement.selectFirst(AUTHOR_NAME_SELECTOR));
    }

    private String parseAuthorProfileUrl(Element postElement) {
        return isShared(postElement)
                ? absUrlFromHref(postElement.selectFirst("a[data-control-name=original_share_actor_container]"))
                : absUrlFromHref(postElement.selectFirst(SHARED_URL_SELECTOR));
    }

    private String parseAuthorConnectionDegree(Element postElement) {
        String connectionDegree = isShared(postElement)
                ? text(postElement.select(CONNECTION_DEGREE_SELECTOR).last())
                : text(postElement.selectFirst(CONNECTION_DEGREE_SELECTOR));

        return normalizeSpace(substringAfter(connectionDegree, "•"));
    }

    private String parseAuthorHeadline(Element postElement) {
        return isShared(postElement)
                ? text(postElement.select(AUTHOR_HEADLINE_SELECTOR).last())
                : text(postElement.selectFirst(AUTHOR_HEADLINE_SELECTOR));
    }

    private String parseContent(Element postElement) {
        String content = isShared(postElement)
                ? text(postElement.select(CONTENT_SELECTOR).last())
                : text(postElement.selectFirst(CONTENT_SELECTOR));

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

    private void handleTwoPostContents(Element postElement, LinkedinPost post) {
        String mainContent = text(postElement.selectFirst(CONTENT_SELECTOR));

        if (isShared(postElement) && postElement.select(CONTENT_SELECTOR).size() == 2) {
            String originContent = "\n!zxz9119origpstehh8!: " + post.getContent();
            String bothContents = mainContent.concat(originContent);
            setMainPost(postElement, post, bothContents);
        }
    }

    private void setMainPost(Element postElement, LinkedinPost post, String bothContents) {
        post.setContent(bothContents);
        post.setAuthorProfileUrl(absUrlFromHref(postElement.selectFirst(SHARED_URL_SELECTOR)));
        post.setAuthorHeadline(text(postElement.selectFirst(AUTHOR_HEADLINE_SELECTOR)));
        post.setAuthorConnectionDegree(text(postElement.selectFirst(CONNECTION_DEGREE_SELECTOR)));
        post.setAuthorProfileName(text(postElement.selectFirst(AUTHOR_NAME_SELECTOR)));
    }
}
