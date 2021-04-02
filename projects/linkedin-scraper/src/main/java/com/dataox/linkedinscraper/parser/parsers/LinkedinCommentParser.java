package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.exceptions.EmptySourceException;
import com.dataox.linkedinscraper.parser.service.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinCommentParser implements LinkedinParser<Set<LinkedinComment>, String> {

    private static final String COMMENT_URL_TEMPLATE = "https://www.linkedin.com/feed/update/urn:li:urnType:ZX?commentUrn=urn%3Ali%3Acomment%3A%28urnType%3AZF%2CZB%29";
    private static final String REPLY_URL_TEMPLATE = "&replyUrn=urn%3Ali%3Acomment%3A%28urnType%3AZX%2CZB%29";

    private final TimeConverter timeConverter;

    @Override
    public Set<LinkedinComment> parse(String source) {
        if (source.isEmpty()) {
            log.error("received empty source", new EmptySourceException("Comment parser shouldn't receive empty source"));
            return null;
        }

        Element commentSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitComments(commentSectionElement).stream()
                .map(commentElement -> getLinkedinComment(commentElement, time))
                .collect(Collectors.toSet());
    }

    private Elements splitComments(Element commentSectionElement) {
        return commentSectionElement.select("article[data-id^=urn]");
    }

    private LinkedinComment getLinkedinComment(Element commentElement, Instant time) {
        String urn = commentElement.attr("data-id");

        LinkedinComment comment = new LinkedinComment();
        comment.setCollectedDate(time);
        comment.setItemSource(commentElement.html());
        comment.setContent(parseContent(commentElement));
        comment.setRelativePublicationDate(parseRelativePublicationDate(commentElement));
        comment.setAbsolutePublicationDate(getAbsolutePublicationDate(comment.getRelativePublicationDate()));
        comment.setNumberOfReactions(parseNumberOfReactions(commentElement));
        comment.setNumberOfReplies(parseNumberOfReplies(commentElement));
        comment.setUrl(getCommentUrl(urn));
        setReplyUrl(commentElement, urn, comment);

        return comment;
    }

    private void setReplyUrl(Element commentElement, String urn, LinkedinComment comment) {
        String parentClass = commentElement.parent().className();

        if (isReply(parentClass)) {
            String parentUrn = getParentUrn(commentElement);
            comment.setUrl(getReplyUrl(urn, getCommentUrl(parentUrn)));
        }
    }

    private boolean isReply(String parentClass) {
        return parentClass.startsWith("replies-list");
    }

    private String getParentUrn(Element commentElement) {
        return commentElement.parents().select("article").stream()
                .map(parentElement -> parentElement.attr("data-id"))
                .collect(Collectors.toList()).get(1);
    }

    private String getCommentUrl(String urn) {
        String[] templateValues = getTemplateValues(urn);
        String urnType = extractUrnType(urn);
        return COMMENT_URL_TEMPLATE.replaceAll("urnType", urnType)
                .replace("ZX", templateValues[0]).replace("ZF", templateValues[0]).replace("ZB", templateValues[1]);
    }

    private String getReplyUrl(String urn, String commentUrl) {
        String[] templateValues = getTemplateValues(urn);
        String urnType = extractUrnType(urn);
        return commentUrl + REPLY_URL_TEMPLATE.replace("urnType", urnType)
                .replace("ZX", templateValues[0]).replace("ZB", templateValues[1]);
    }

    private String[] getTemplateValues(String urn) {
        String urnType = extractUrnType(urn);
        return substringBetween(urn, urnType + ":", ")").split(",");
    }

    private String extractUrnType(String urn) {
        return substringBetween(urn, ":(", ":");
    }

    private String parseContent(Element commentElement) {
        return text(commentElement.selectFirst("p.feed-shared-main-content"));
    }

    private String parseRelativePublicationDate(Element commentElement) {
        return text(commentElement.selectFirst("time.comments-comment-item__timestamp"));
    }

    private Instant getAbsolutePublicationDate(String relativePublicationDate) {
        Objects.requireNonNull(relativePublicationDate, "Time converter received null relative date " +
                " in Post parser");
        return timeConverter.getAbsoluteTime(relativePublicationDate);
    }

    private int parseNumberOfReactions(Element commentElement) {
        String reaction = text(commentElement.selectFirst("img.reactions-icon + span"));
        return isNotBlank(reaction)
                ? Integer.parseInt(reaction.replaceAll("\\D+", ""))
                : 0;
    }

    private int parseNumberOfReplies(Element commentElement) {
        String replies = text(commentElement.selectFirst("button.comments-comment-social-bar__replies-count > span"));
        return isNotBlank(replies)
                ? Integer.parseInt(replies.replaceAll("\\D+", ""))
                : 0;
    }
}
