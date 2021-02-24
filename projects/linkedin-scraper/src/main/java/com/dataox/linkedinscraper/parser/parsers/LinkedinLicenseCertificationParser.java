package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinLicenseCertification;
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
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;

@Service
@Slf4j
public class LinkedinLicenseCertificationParser implements LinkedinParser<List<LinkedinLicenseCertification>, String> {

    @Override
    public List<LinkedinLicenseCertification> parse(String source) {
        if (isBlank(source)) {
            log.info("{} received empty source", this.getClass().getSimpleName());
            return Collections.emptyList();
        }

        Element certificationSectionElement = toElement(source);
        Instant time = Instant.now();

        return splitCertification(certificationSectionElement).stream()
                .map(element -> getLinkedinLicenseCertification(element, time))
                .collect(Collectors.toList());
    }

    private Elements splitCertification(Element certificationSectionElement) {
        return certificationSectionElement.select("header + ul > li");
    }

    private LinkedinLicenseCertification getLinkedinLicenseCertification(Element certificationElement, Instant time) {
        LinkedinLicenseCertification licenseCertification = new LinkedinLicenseCertification();

        licenseCertification.setUpdatedAt(time);
        licenseCertification.setItemSource(certificationElement.html());
        licenseCertification.setName(parseName(certificationElement));
        licenseCertification.setIssuer(parseIssuer(certificationElement));
        licenseCertification.setIssuerProfileUrl(parseIssuerProfileUrl(certificationElement));
        licenseCertification.setIssuedDate(parseIssueDate(certificationElement));
        licenseCertification.setExpirationDate(parseExpirationDate(certificationElement));
        licenseCertification.setCredentialId(parseCredentialId(certificationElement));

        return licenseCertification;
    }

    private String parseName(Element certificationElement) {
        return text(certificationElement.selectFirst("h3"));
    }

    private String parseIssuer(Element certificationElement) {
        return text(certificationElement.selectFirst("h3 + p > span:contains(Issuing authority) + span"));
    }

    private String parseIssuerProfileUrl(Element experienceElement) {
        return absUrlFromHref(experienceElement.selectFirst("a[data-control-name=background_details_certification]"));
    }

    private String parseIssueDate(Element experienceElement) {
        String issueDate = text(experienceElement.selectFirst("span:contains(Issued date and) + span"));
        return nonNull(issueDate)
                ? substringBetween(issueDate, "Issued ", parseExpirationDate(experienceElement))
                : null;
    }

    private String parseExpirationDate(Element experienceElement) {
        return text(experienceElement.selectFirst("span:contains(Issued date and) + span > span"));
    }

    private String parseCredentialId(Element experienceElement) {
        String credentialId = text(experienceElement.selectFirst("span:contains(Credential Identifier) + span"));
        return nonNull(credentialId)
                ? substringAfter(credentialId, "Credential ID ").trim()
                : null;
    }
}
