package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinRecommendation;
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
public class LinkedinRecommendationParser implements LinkedinParser<List<LinkedinRecommendation>, List<RecommendationsSource>> {

    @Override
    public List<LinkedinRecommendation> parse(List<RecommendationsSource> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .flatMap(recommendationsSource -> resolveRecommendationsByType(time, recommendationsSource))
                .collect(Collectors.toList());
    }

    private Stream<LinkedinRecommendation> resolveRecommendationsByType(Instant time, RecommendationsSource recommendationsSource) {
        Element recommendationSectionElement = toElement(recommendationsSource.getSource());

        return splitRecommendation(recommendationSectionElement).stream()
                .map(recommendationElement ->
                        getLinkedinRecommendation(recommendationElement, time, recommendationsSource.getType())
                );
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
