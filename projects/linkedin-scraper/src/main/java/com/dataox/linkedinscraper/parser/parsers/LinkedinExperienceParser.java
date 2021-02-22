package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinExperience;
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
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
public class LinkedinExperienceParser implements LinkedinParser<List<LinkedinExperience>, String> {

    @Override
    public List<LinkedinExperience> parse(String source) {
        if (source.isEmpty()) {
            return Collections.emptyList();
        }

        Element experienceSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitExperience(experienceSectionElement).stream()
                .flatMap(element -> {
                    if (isMultiplePositions(element)) {
                        return getAllPositions(time, element);
                    }
                    return Stream.of(getLinkedinExperience(element, time));
                })
                .collect(Collectors.toList());
    }

    private Elements splitExperience(Element experienceSectionElement) {
        return experienceSectionElement.select("header + ul > li");
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
                    return experience;
                });
    }

    private Elements splitMultiplePositions(Element experienceElement) {
        return experienceElement.select(".pv-entity__position-group.mt2 > li");
    }

    private LinkedinExperience getLinkedinExperience(Element educationElement, Instant time) {
        LinkedinExperience experience = new LinkedinExperience();

        experience.setUpdatedAt(time);
        experience.setItemSource(educationElement.html());
        experience.setCompanyName(parseCompanyName(educationElement));
        experience.setCompanyProfileUrl(parseCompanyProfileUrl(educationElement));
        experience.setPosition(parsePosition(educationElement));
        experience.setDateStarted(parseDateStarted(educationElement));
        experience.setDateFinished(parseDateFinished(educationElement));
        experience.setLocation(parseLocation(educationElement));
        experience.setDescription(parseDescription(educationElement));

        return experience;
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

    private String parseDateStarted(Element experienceElement) {
        String dateRange = parseDateRange(experienceElement);
        return substringBefore(dateRange, " – ").trim();
    }

    private String parseDateFinished(Element experienceElement) {
        String dateRange = parseDateRange(experienceElement);
        return substringAfter(dateRange, " – ").trim();
    }

    private String parseLocation(Element experienceElement) {
        return text(experienceElement.selectFirst(".pv-entity__location > span + span"));
    }

    private String parseDescription(Element experienceElement) {
        return text(experienceElement.selectFirst(".pv-entity__extra-details"));
    }
}
