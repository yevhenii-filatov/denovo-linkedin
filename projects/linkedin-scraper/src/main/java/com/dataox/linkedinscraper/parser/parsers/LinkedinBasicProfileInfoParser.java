package com.dataox.linkedinscraper.parser.parsers;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinBasicProfileInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.dataox.jsouputils.JsoupUtils.text;
import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.toElement;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
@Slf4j
public class LinkedinBasicProfileInfoParser implements LinkedinParser<LinkedinBasicProfileInfo, List<String>> {

    @Override
    public LinkedinBasicProfileInfo parse(List<String> source) {
        if (source.isEmpty()) {
            log.info("received empty source");
            return null;
        }

        String headerSectionSource = source.get(0);
        Element headerElement = toElement(headerSectionSource);

        LinkedinBasicProfileInfo basicProfileInfo = new LinkedinBasicProfileInfo();
        basicProfileInfo.setUpdatedAt(Instant.now());
        basicProfileInfo.setHeaderSectionSource(headerSectionSource);
        basicProfileInfo.setFullName(parseFullName(headerElement));
        basicProfileInfo.setNumberOfConnections(parseNumberOfConnections(headerElement));
        basicProfileInfo.setLocation(parseLocation(headerElement));
        setAboutIfExists(source, basicProfileInfo);

        return basicProfileInfo;
    }

    private void setAboutIfExists(List<String> source, LinkedinBasicProfileInfo basicProfileInfo) {
        if (source.size() == 2) {
            String aboutSectionSource = source.get(1);
            Element aboutElement = toElement(aboutSectionSource);
            basicProfileInfo.setAbout(parseAbout(aboutElement));
            basicProfileInfo.setAboutSectionSource(aboutSectionSource);
        }
    }

    private String parseFullName(Element headerElement) {
        return text(headerElement.selectFirst("div > div:nth-child(1) > h1"));
    }

    private String parseNumberOfConnections(Element headerElement) {
        return substringBefore(text(headerElement.selectFirst("span:contains(connections)")), "connections").trim();
    }

    private String parseLocation(Element headerElement) {
        return text(headerElement.selectFirst("span.text-body-small.inline.t-black--light.break-words"));
    }

    private String parseAbout(Element aboutElement) {
        return substringBefore(text(aboutElement.selectFirst("header + p")), " ... see more");
    }
}
