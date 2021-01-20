package com.dataox.googleserp.service.parsing;

import com.dataox.googleserp.configuration.properties.SearchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.dataox.jsouputils.JsoupUtils.absUrlFromHref;
import static com.dataox.jsouputils.JsoupUtils.text;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Slf4j
@RequiredArgsConstructor
@Service("GOOGLE-HTML-Parser")
public class GoogleSearchResultHtmlParser extends AbstractSearchResultParser<Element> {
    private final SearchProperties searchProperties;

    @Override
    public Elements splitResults(Element searchResultsPage) {
        return searchResultsPage.select("#rso .g").stream()
                .limit(searchProperties.getRequiredResultsCount())
                .collect(Collectors.toCollection(Elements::new));
    }

    @Override
    public String fetchUrl(Element searchResultElement) {
        return absUrlFromHref(searchResultElement.selectFirst("a:has(h3)"));
    }

    @Override
    public String fetchTitle(Element searchResultElement) {
        return text(searchResultElement.selectFirst("a h3"));
    }

    @Override
    public String fetchDescription(Element searchResultElement) {
        return text(searchResultElement.selectFirst("div:eq(0) + div span > span"));
    }
}
