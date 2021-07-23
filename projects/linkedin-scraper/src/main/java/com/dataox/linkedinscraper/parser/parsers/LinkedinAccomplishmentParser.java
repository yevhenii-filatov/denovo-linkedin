package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinAccomplishment;
import com.dataox.linkedinscraper.parser.service.mappers.LinkedinAccomplishmentTypeMapper;
import com.dataox.linkedinscraper.parser.utils.ParsingUtils;
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

import static com.dataox.jsouputils.JsoupUtils.text;

@Slf4j
@RequiredArgsConstructor
@Service
public class LinkedinAccomplishmentParser implements LinkedinParser<List<LinkedinAccomplishment>, List<String>> {

    private final LinkedinAccomplishmentTypeMapper mapper;

    @Override
    public List<LinkedinAccomplishment> parse(List<String> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return Collections.emptyList();
        }

        Instant time = Instant.now();

        return source.stream()
                .map(ParsingUtils::toElement)
                .flatMap(accomplishmentSectionElement -> getAccomplishmentsWithinType(time, accomplishmentSectionElement))
                .collect(Collectors.toList());
    }

    private Stream<LinkedinAccomplishment> getAccomplishmentsWithinType(Instant time, Element accomplishmentSectionElement) {
        String type = parseType(accomplishmentSectionElement);

        return splitAccomplishment(accomplishmentSectionElement).stream()
                .map(accomplishmentElement -> getLinkedinAccomplishment(time, accomplishmentElement, type));
    }

    private LinkedinAccomplishment getLinkedinAccomplishment(Instant time, Element accomplishmentElement, String type) {
        LinkedinAccomplishment accomplishment = new LinkedinAccomplishment();

        accomplishment.setUpdatedAt(time);
        accomplishment.setItemSource(accomplishmentElement.html());
        accomplishment.setLinkedinAccomplishmentType(mapper.map(type));
        accomplishment.setTitle(parseTitle(accomplishmentElement));
        accomplishment.setDescription(parseDescription(accomplishmentElement));

        return accomplishment;
    }

    private Elements splitAccomplishment(Element accomplishmentSectionElement) {
        return accomplishmentSectionElement.select("ul.pv-accomplishments-block__list  > li");
    }

    private String parseType(Element accomplishmentElement) {
        return text(accomplishmentElement.selectFirst(".pv-accomplishments-block__title"));
    }

    private String parseTitle(Element accomplishmentElement) {
        return accomplishmentElement.selectFirst(".pv-accomplishment-entity__title").ownText();
    }

    private String parseDescription(Element accomplishmentElement) {
        String description = text(accomplishmentElement.select(
                ".pv-accomplishment-entity__publisher," +
                        ".pv-accomplishment-entity__description," +
                        ".pv-accomplishment-entity__proficiency"
        ));
        description += " " + text(accomplishmentElement.select(".pv-accomplishment-entity__date"))
                + " " + text(accomplishmentElement.select(".pv-accomplishment-entity__issuer"));

        return description.replaceAll("null", "").trim();
    }
}
