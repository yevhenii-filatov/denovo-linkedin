package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinRecommendation;
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

@Service
public class LinkedinRecommendationParser implements LinkedinParser<List<LinkedinRecommendation>, Map<String, String>> {

    @Override
    public List<LinkedinRecommendation> parse(Map<String, String> source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.entrySet().stream()
                .flatMap(entry -> {
                    Element recommendationSectionElement = toElement(entry.getValue());

                    return splitRecommendation(recommendationSectionElement).stream()
                            .map(recommendationElement ->
                                    getLinkedinRecommendation(recommendationElement, time, entry.getKey())
                            );
                })
                .collect(Collectors.toList());
    }

    private Elements splitRecommendation(Element recommendationSectionElement) {
        return recommendationSectionElement.select("ul.section-info > li");
    }

    private LinkedinRecommendation getLinkedinRecommendation(Element recommendationElement, Instant time, String type) {
        LinkedinRecommendation recommendation = new LinkedinRecommendation();

        recommendation.setUpdatedAt(time);
        recommendation.setItemSource(recommendationElement.html());
        recommendation.setType(type);
        recommendation.setPersonFullName(parsePersonFullName(recommendationElement));
        recommendation.setPersonProfileUrl(parseProfileUrl(recommendationElement));
        recommendation.setPersonHeadline(parsePersonHeadLine(recommendationElement));
        recommendation.setPersonExtraInfo(parsePersonExtraInfo(recommendationElement));
        recommendation.setDescription(parseDescription(recommendationElement));

        return recommendation;
    }

    private String parsePersonFullName(Element recommendationElement) {
        return text(recommendationElement.selectFirst("div.pv-recommendation-entity__detail > h3"));
    }

    private String parseProfileUrl(Element recommendationElement) {
        return absUrlFromHref(recommendationElement.selectFirst("a[data-control-name=recommendation_details_profile]"));
    }

    private String parsePersonHeadLine(Element recommendationElement) {
        return text(recommendationElement.selectFirst("p.pv-recommendation-entity__headline"));
    }

    private String parsePersonExtraInfo(Element recommendationElement) {
        return text(recommendationElement.selectFirst("p.pv-recommendation-entity__headline + p"));
    }

    private String parseDescription(Element recommendationElement) {
        return text(recommendationElement.selectFirst(".pv-recommendation-entity__text > div"))
                .replace(" See less", "")
                .replace("... See more", "")
                .trim();
    }
}
