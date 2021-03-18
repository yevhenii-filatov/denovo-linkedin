package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinExperience;
import com.dataox.linkedinscraper.parser.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
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
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkedinExperienceParser implements LinkedinParser<List<LinkedinExperience>, String> {

    private final TimeConverter timeConverter;

    @Override
    public List<LinkedinExperience> parse(String source) {
        if (isBlank(source)) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Element experienceSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitExperience(experienceSectionElement).stream()
                .flatMap(element -> resolveMultiplePositions(time, element))
                .collect(Collectors.toList());
    }

    private Elements splitExperience(Element experienceSectionElement) {
        return experienceSectionElement.select("header + ul > li");
    }

    private Stream<LinkedinExperience> resolveMultiplePositions(Instant time, Element element) {
        if (isMultiplePositions(element)) {
            return getAllPositions(time, element);
        }
        return Stream.of(getLinkedinExperience(element, time));
    }

    private boolean isMultiplePositions(Element element) {
        return nonNull(element.selectFirst("h3 > span:contains(Company Name)"));
    }

    private Stream<LinkedinExperience> getAllPositions(Instant time, Element element) {
        return splitMultiplePositions(element).stream()
                .map(innerElement -> {
                    LinkedinExperience experience = getLinkedinExperience(innerElement, time);
                    experience.setCompanyName(parseCompanyName(element));
                    experience.setCompanyProfileUrl(parseCompanyProfileUrl(element));
                    experience.setJobType(parseJobTypeForMultiplePositions(element));
                    return experience;
                });
    }

    private Elements splitMultiplePositions(Element experienceElement) {
        return experienceElement.select(".pv-entity__position-group.mt2 > li");
    }

    private LinkedinExperience getLinkedinExperience(Element experienceElement, Instant time) {
        LinkedinExperience experience = new LinkedinExperience();

        experience.setUpdatedAt(time);
        experience.setItemSource(experienceElement.html());
        experience.setJobType(parseJobType(experienceElement));
        experience.setCompanyName(parseCompanyName(experienceElement));
        experience.setCompanyProfileUrl(parseCompanyProfileUrl(experienceElement));
        experience.setPosition(parsePosition(experienceElement));

        String dateRange = parseDateRange(experienceElement);
        if (isNoneBlank(dateRange)) {
            experience.setDateStarted(getDateStarted(dateRange));
            experience.setDateFinished(getDateFinished(dateRange));
            experience.setDateStartedTimestamp(timeConverter.toLocalDate(experience.getDateStarted()));
            if(!experience.getDateFinished().equals("Present")) {
                experience.setDateFinishedTimestamp(timeConverter.toLocalDate(experience.getDateFinished()));
            }
            experience.setTotalDuration(parseTotalDuration(experienceElement));
        }

        experience.setLocation(parseLocation(experienceElement));
        experience.setDescription(parseDescription(experienceElement));

        return experience;
    }

    private String parseJobType(Element experienceElement) {
        return text(experienceElement.selectFirst("span.pv-entity__secondary-title"));
    }

    private String parseJobTypeForMultiplePositions(Element experienceElement) {
        return text(experienceElement.selectFirst(".pv-entity__position-group.mt2 > li .pv-entity__summary-info-v2 > h4.t-14.t-black.t-normal"));
    }

    private String parseCompanyName(Element experienceElement) {
        Element companyNameElement = experienceElement.selectFirst("h3 > span:contains(Company Name) + span");
        return nonNull(companyNameElement)
                ? text(companyNameElement)
                : text(experienceElement.selectFirst("p:contains(Company Name) + p"));
    }

    private String parseCompanyProfileUrl(Element experienceElement) {
        return absUrlFromHref(experienceElement.selectFirst("a[data-control-name=background_details_company]"));
    }

    private String parsePosition(Element experienceElement) {
        return text(experienceElement.selectFirst("h3:not(:contains(Company name))"))
                .replace("Title", "")
                .trim();
    }

    private String parseDateRange(Element experienceElement) {
        return text(experienceElement.selectFirst(".pv-entity__date-range > span + span"));
    }

    private String getDateStarted(String dateRange) {
        return substringBefore(dateRange, " – ").trim();
    }

    private String getDateFinished(String dateRange) {
        return substringAfter(dateRange, " – ").trim();
    }

    private String parseTotalDuration(Element experienceElement) {
        return text(experienceElement.selectFirst("h4:contains(Employment Duration) > span + span"));
    }

    private String parseLocation(Element experienceElement) {
        return text(experienceElement.selectFirst(".pv-entity__location > span + span"));
    }

    private String parseDescription(Element experienceElement) {
        String description = text(experienceElement.selectFirst(".pv-entity__extra-details"));
        return nonNull(description)
                ? description.replaceAll("see less", "")
                : null;
    }
}
