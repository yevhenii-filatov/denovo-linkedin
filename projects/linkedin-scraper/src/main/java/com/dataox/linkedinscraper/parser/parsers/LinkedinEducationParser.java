package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEducation;
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
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
public class LinkedinEducationParser implements LinkedinParser<List<LinkedinEducation>, String> {

    @Override
    public List<LinkedinEducation> parse(String source) {
        if (isBlank(source)) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Element educationSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitEducation(educationSectionElement).stream()
                .map(element -> getLinkedinEducation(element, time))
                .collect(Collectors.toList());
    }

    private Elements splitEducation(Element educationSectionElement) {
        return educationSectionElement.select("li.pv-profile-section__list-item");
    }

    private LinkedinEducation getLinkedinEducation(Element educationElement, Instant time) {
        LinkedinEducation education = new LinkedinEducation();

        education.setUpdatedAt(time);
        education.setItemSource(educationElement.html());
        education.setInstitutionName(parseInstitutionName(educationElement));
        education.setInstitutionProfileUrl(parseInstitutionProfileUrl(educationElement));
        education.setDegree(parseDegree(educationElement));
        education.setFieldOfStudy(parseFieldOfStudy(educationElement));
        education.setStartedYear(parseStartedYear(educationElement));
        education.setFinishedYear(parseFinishedYear(educationElement));
        education.setActivitiesAndSocieties(parseActivitiesAndSocieties(educationElement));
        education.setActivitiesAndSocieties(parseActivitiesAndSocieties(educationElement));
        education.setDescription(parseDescription(educationElement));

        return education;
    }

    private String parseInstitutionName(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__degree-info > h3"));
    }

    private String parseInstitutionProfileUrl(Element educationElement) {
        return absUrlFromHref(educationElement.selectFirst("a[data-control-name=background_details_school]"));
    }

    private String parseDegree(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__degree-info > p:contains(degree name) > span + span"));
    }

    private String parseFieldOfStudy(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__degree-info > p:contains(Field Of Study) > span + span"));
    }

    private String parseStartedYear(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__dates time:lt(1)"));
    }

    private String parseFinishedYear(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__dates time:lt(1) + time"));
    }

    private String parseDescription(Element educationElement) {
        return text(educationElement.selectFirst(".pv-entity__description"));
    }

    private String parseActivitiesAndSocieties(Element educationElement) {
        return text(educationElement.selectFirst(".activities-societies"));
    }
}
