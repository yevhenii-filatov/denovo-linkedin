package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinInterest;
import com.dataox.linkedinscraper.parser.utils.sources.InterestsSource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;

@Service
@Slf4j
public class LinkedinInterestParser implements LinkedinParser<List<LinkedinInterest>, List<InterestsSource>> {

    @Override
    public List<LinkedinInterest> parse(List<InterestsSource> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .flatMap(interestsSource -> resolveInterestsByType(time, interestsSource))
                .collect(Collectors.toList());
    }

    private Stream<LinkedinInterest> resolveInterestsByType(Instant time, InterestsSource interestsSource) {
        Element interestsSectionElement = toElement(interestsSource.getSource());

        return splitInterests(interestsSectionElement).stream()
                .map(interestsElement -> getLinkedinInterest(interestsElement, time, interestsSource.getCategory()));
    }

    private Elements splitInterests(Element interestsSectionElement) {
        return interestsSectionElement.html().contains("role=\"dialog\"")
                ? interestsSectionElement.select("ul > li.entity-list-item")
                : interestsSectionElement.select("ul > li.pv-interest-entity");
    }

    private LinkedinInterest getLinkedinInterest(Element interestsElement, Instant time, String type) {
        LinkedinInterest interest = new LinkedinInterest();

        interest.setUpdatedAt(time);
        interest.setItemSource(interestsElement.html());
        interest.setType(type.toUpperCase());
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
        return text(interestsElement.selectFirst("p.pv-entity__follower-count")).replaceAll("\\D+", "");
    }
}
