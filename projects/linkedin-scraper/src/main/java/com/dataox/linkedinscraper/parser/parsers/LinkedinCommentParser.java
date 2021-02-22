package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinComment;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Service
@RequiredArgsConstructor
public class LinkedinCommentParser implements LinkedinParser<List<LinkedinComment>, String> {

    private static final String COMMENT_URL_TEMPLATE = "https://www.linkedin.com/feed/update/urn:li:activity:ZX?commentUrn=urn%3Ali%3Acomment%3A%28activity%3AZF%2CZB%29";
    private static final String REPLY_URL_TEMPLATE = "&replyUrn=urn%3Ali%3Acomment%3A%28activity%3AZX%2CZB%29";

    private final TimeConverter timeConverter;

    @Override
    public List<LinkedinComment> parse(String source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Element commentSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitComments(commentSectionElement).stream()
                .map(commentElement -> {
                    String urn = commentElement.attr("data-id");
                    LinkedinComment comment = getLinkedinComment(commentElement, time, urn);
                    String parentClass = commentElement.parent().className();

                    if (isReply(parentClass)) {
                        String parentUrn = getParentUrn(commentElement);
                        comment.setUrl(getReplyUrl(urn, getCommentUrl(parentUrn)));
                    }

                    return comment;
                })
                .collect(Collectors.toList());
    }

    private Elements splitComments(Element commentSectionElement) {
        return commentSectionElement.select("article[data-id^=urn]");
    }

    private LinkedinComment getLinkedinComment(Element contentElement, Instant time, String urn) {
        LinkedinComment comment = new LinkedinComment();

        comment.setCollectedDate(time);
        comment.setItemSource(contentElement.html());
        comment.setContent(parseContent(contentElement));
        comment.setRelativePublicationDate(parseRelativePublicationDate(contentElement));
        comment.setAbsolutePublicationDate(getAbsolutePublicationDate(contentElement));
        comment.setNumberOfReactions(parseNumberOfReactions(contentElement));
        comment.setNumberOfReplies(parseNumberOfReplies(contentElement));
        comment.setUrl(getCommentUrl(urn));

        return comment;
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
        return COMMENT_URL_TEMPLATE.replace("ZX", templateValues[0]).replace("ZF", templateValues[0]).replace("ZB", templateValues[1]);
    }

    private String getReplyUrl(String urn, String commentUrl) {
        String[] templateValues = getTemplateValues(urn);
        return commentUrl + REPLY_URL_TEMPLATE.replace("ZX", templateValues[0]).replace("ZB", templateValues[1]);
    }

    private String[] getTemplateValues(String urn) {
        return substringBetween(urn, "activity:", ")").split(",");
    }

    private String parseContent(Element commentElement) {
        return text(commentElement.selectFirst("p.feed-shared-main-content > span"));
    }

    private String parseRelativePublicationDate(Element commentElement) {
        return text(commentElement.selectFirst("time.comments-comment-item__timestamp"));
    }

    private Instant getAbsolutePublicationDate(Element commentElement) {
        return timeConverter.getAbsoluteTime(parseRelativePublicationDate(commentElement));
    }

    private int parseNumberOfReactions(Element commentElement) {
        String reaction = text(commentElement.selectFirst("img.reactions-icon + span"));
        return isNotBlank(reaction)
                ? Integer.parseInt(reaction)
                : 0;
    }

    private int parseNumberOfReplies(Element commentElement) {
        String replies = text(commentElement.selectFirst("button.comments-comment-social-bar__replies-count > span"));
        return isNotBlank(replies)
                ? Integer.parseInt(replies.replaceAll("\\D+", ""))
                : 0;
    }
}