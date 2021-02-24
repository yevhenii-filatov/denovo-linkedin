package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinVolunteerExperience;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.*;

@Service
@Slf4j
public class LinkedinVolunteerExperienceParser implements LinkedinParser<List<LinkedinVolunteerExperience>, String> {

    @Override
    public List<LinkedinVolunteerExperience> parse(String source) {
        if (isBlank(source)) {
            log.info("{} received empty source", this.getClass().getSimpleName());
            return Collections.emptyList();
        }

        Element volunteerSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitExperience(volunteerSectionElement).stream()
                .map(element -> getLinkedinVolunteerExperience(element, time))
                .collect(Collectors.toList());
    }

    private Elements splitExperience(Element volunteerSectionElement) {
        return volunteerSectionElement.select("header + ul > li");
    }

    private LinkedinVolunteerExperience getLinkedinVolunteerExperience(Element volunteerElement, Instant time) {
        LinkedinVolunteerExperience volunteerExperience = new LinkedinVolunteerExperience();

        volunteerExperience.setUpdatedAt(time);
        volunteerExperience.setItemSource(volunteerElement.html());
        volunteerExperience.setCompanyName(parseCompanyName(volunteerElement));
        volunteerExperience.setCompanyProfileUrl(parseCompanyProfileUrl(volunteerElement));
        volunteerExperience.setPosition(parsePosition(volunteerElement));
        volunteerExperience.setDateStarted(parseDateStarted(volunteerElement));
        volunteerExperience.setDateFinished(parseDateFinished(volunteerElement));
        volunteerExperience.setTotalDuration(parseTotalDuration(volunteerElement));
        volunteerExperience.setDescription(parseDescription(volunteerElement));
        volunteerExperience.setFieldOfActivity(parseFieldOfActivity(volunteerElement));

        return volunteerExperience;
    }

    private String parseCompanyName(Element volunteerElement) {
        return text(volunteerElement.selectFirst("h4 > span:contains(Company Name) + span"));
    }

    private String parseCompanyProfileUrl(Element volunteerElement) {
        return absUrlFromHref(volunteerElement.selectFirst("a[data-control-name=background_details_company]"));
    }

    private String parsePosition(Element volunteerElement) {
        return text(volunteerElement.selectFirst("h3"));
    }

    private String parseDateRange(Element volunteerElement) {
        return text(volunteerElement.selectFirst("h4 > span:contains(Dates volunteered) + span"));
    }

    private String parseDateStarted(Element volunteerElement) {
        String dateRange = parseDateRange(volunteerElement);
        return substringBefore(dateRange, " – ").trim();
    }

    private String parseDateFinished(Element volunteerElement) {
        String dateRange = parseDateRange(volunteerElement);
        return substringAfter(dateRange, " – ").trim();
    }

    private String parseTotalDuration(Element volunteerElement) {
        return text(volunteerElement.selectFirst("h4 > span:contains(Volunteer duration) + span"));
    }

    private String parseFieldOfActivity(Element volunteerElement) {
        return text(volunteerElement.selectFirst("h4 > span:contains(Cause) + span"));
    }

    private String parseDescription(Element volunteerElement) {
        return text(volunteerElement.selectFirst("div.pv-entity__extra-details > p"));
    }
}
