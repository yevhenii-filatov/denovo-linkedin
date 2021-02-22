package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinInterest;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
public class LinkedinInterestParser implements LinkedinParser<List<LinkedinInterest>, Map<String, String>> {

    @Override
    public List<LinkedinInterest> parse(Map<String, String> source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.entrySet().stream()
                .flatMap(entry -> {
                    Element interestsSectionElement = toElement(entry.getValue());

                    return splitInterests(interestsSectionElement).stream()
                            .map(interestsElement -> {
                                String interestType = entry.getKey();
                                return getLinkedinInterest(interestsElement, time, interestType);
                            });
                })
                .collect(Collectors.toList());
    }

    private Elements splitInterests(Element interestsSectionElement) {
        return interestsSectionElement.attr("role").equals("dialog")
                ? interestsSectionElement.select("ul > li.pv-interest-entity")
                : interestsSectionElement.select("ul > li.entity-list-item");
    }

    private LinkedinInterest getLinkedinInterest(Element interestsElement, Instant time, String type) {
        LinkedinInterest interest = new LinkedinInterest();

        interest.setUpdatedAt(time);
        interest.setItemSource(interestsElement.html());
        interest.setType(type);
        interest.setName(parseName(interestsElement));
        interest.setProfileUrl(parseProfileUrl(interestsElement));
        interest.setHeadline(parseHeadline(interestsElement));
        interest.setNumberOfFollowers(parseNumberOfFollowers(interestsElement));

        return interest;
    }

    private String parseName(Element interestsElement) {
        return text(interestsElement.selectFirst(".pv-entity__summary-title-text"));
    }

    private String parseProfileUrl(Element interestsElement) {
        return absUrlFromHref(interestsElement.selectFirst("a[data-control-name*=interests]"));
    }

    private String parseHeadline(Element interestsElement) {
        return text(interestsElement.selectFirst("p.pv-entity__occupation"));
    }

    private String parseNumberOfFollowers(Element interestsElement) {
        return substringBefore(text(interestsElement.selectFirst("p.pv-entity__follower-count")), " followers");
    }
}
