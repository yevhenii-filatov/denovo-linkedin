package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinEndorsement;
import com.dataox.linkedinscraper.parser.exceptions.EmptySourceException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;

@Service
@Slf4j
public class LinkedinEndorsementParser implements LinkedinParser<List<LinkedinEndorsement>, String> {

    @Override
    public List<LinkedinEndorsement> parse(String source) {
        if (source.isEmpty()) {
            log.error("received empty source", new EmptySourceException("Endorsement should receive source"));
            return null;
        }

        Element endorsementSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitEndorsements(endorsementSectionElement).stream()
                .map(element -> getLinkedinEndorsement(element, time))
                .collect(Collectors.toList());
    }

    private Elements splitEndorsements(Element endorsementSectionElement) {
        return endorsementSectionElement.select("ul > li");
    }

    private LinkedinEndorsement getLinkedinEndorsement(Element endorsementElement, Instant time) {
        LinkedinEndorsement endorsement = new LinkedinEndorsement();

        endorsement.setUpdatedAt(time);
        endorsement.setItemSource(endorsementElement.html());
        endorsement.setEndorserFullName(parseEndorserFullName(endorsementElement));
        endorsement.setEndorserHeadline(parseEndorserHeadline(endorsementElement));
        endorsement.setEndorserConnectionDegree(parseEndorserConnectionDegree(endorsementElement));
        endorsement.setEndorserProfileUrl(parseProfileUrl(endorsementElement));

        return endorsement;
    }

    private String parseEndorserFullName(Element endorsementElement) {
        return text(endorsementElement.selectFirst("div.pv-endorsement-entity__name > span:lt(1)"));
    }

    private String parseEndorserHeadline(Element endorsementElement) {
        return text(endorsementElement.selectFirst("div.pv-endorsement-entity__headline"));
    }

    private String parseEndorserConnectionDegree(Element endorsementElement) {
        return text(endorsementElement.selectFirst("span.dist-value"));
    }

    private String parseProfileUrl(Element endorsementElement) {
        return absUrlFromHref(endorsementElement.selectFirst("a[data-control-name*=skills_endorsement]"));
    }
}
