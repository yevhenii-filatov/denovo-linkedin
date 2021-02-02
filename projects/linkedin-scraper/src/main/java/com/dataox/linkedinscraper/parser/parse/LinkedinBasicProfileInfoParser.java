package com.dataox.linkedinscraper.parser.parse;

import com.dataox.linkedinscraper.parser.LinkedinParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinBasicProfileInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.dataox.jsouputils.JsoupUtils.text;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
public class LinkedinBasicProfileInfoParser implements LinkedinParser<LinkedinBasicProfileInfo, List<String>> {

    @Override
    public List<LinkedinBasicProfileInfo> parse(List<String> source) {
        LinkedinBasicProfileInfo basicProfileInfo = new LinkedinBasicProfileInfo();

        String headerSectionSource = source.get(0);
        String aboutSectionSource = source.get(1);

        Element headerElement = Jsoup.parse(headerSectionSource).selectFirst("body");
        Element aboutElement = Jsoup.parse(aboutSectionSource).selectFirst("body");

        basicProfileInfo.setHeaderSectionSource(headerSectionSource);
        basicProfileInfo.setAboutSectionSource(aboutSectionSource);
        basicProfileInfo.setFullName(parseFullName(headerElement));
        basicProfileInfo.setNumberOfConnections(parseNumberOfConnections(headerElement));
        basicProfileInfo.setCachedImageUrl(parseImgUrl(headerElement));
        basicProfileInfo.setAbout(parseAbout(aboutElement));
        basicProfileInfo.setUpdatedAt(Instant.now());

        return Collections.singletonList(basicProfileInfo);
    }

    private String parseFullName(Element headerElement) {
        return text(headerElement.selectFirst(".flex-1.mr5 > ul > li"));
    }

    private String parseNumberOfConnections(Element headerElement) {
        return text(headerElement.selectFirst(".inline-block > a > span:contains(connections)"));
    }

    private String parseImgUrl(Element headerElement) {
        return headerElement.selectFirst("div > img[title]").attr("src");
    }

    private String parseAbout(Element aboutElement) {
        return substringBefore(text(aboutElement.selectFirst("header + p"))," ... see more");
    }
}
