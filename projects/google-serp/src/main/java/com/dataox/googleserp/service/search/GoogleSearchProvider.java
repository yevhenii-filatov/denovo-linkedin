package com.dataox.googleserp.service.search;

import com.dataox.googleserp.configuration.properties.ProxyProperties;
import com.dataox.googleserp.configuration.properties.SearchProperties;
import com.dataox.googleserp.model.search.SearchQuery;
import com.dataox.googleserp.service.parsing.GoogleSearchResultHtmlParser;
import com.dataox.googleserp.service.parsing.SearchResultParser;
import com.dataox.googleserp.util.BeanUtils;
import com.dataox.googleserp.util.NotificationUtils;
import com.dataox.notificationservice.service.NotificationsService;
import com.dataox.okhttputils.OkHttpTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSearchProvider extends AbstractSearchProvider<Element> {
    private static final String OPERIA_ENDPOINT_TEMPLATE = "https://run.operia.io/%s/?ua=%s&url=%s";
    private static final String GOOGLE_SEARCH_URL_TEMPLATE = "https://google.com/search?hl=en&q=%s";
    private final NotificationsService notificationService;
    private final OkHttpTemplate okHttpTemplate;
    private final ProxyProperties proxyProperties;
    private final SearchProperties searchProperties;
    private int currentAttempt = 1;

    @Override
    public Element resolveSearchResultsContainer(String queryUrl) {
        try {
            String pageSource = okHttpTemplate.get(queryUrl);
            return Jsoup.parse(pageSource, queryUrl);
        } catch (IOException e) {
            if (currentAttempt >= searchProperties.getRetryAttempts()) {
                notificationService.sendInternal(NotificationUtils.createSearchErrorMessage(e, queryUrl));
                return null;
            }
            log.error("SCRAPING ATTEMPT #{} FAILED FOR URL {}", currentAttempt, queryUrl);
            currentAttempt++;
            return resolveSearchResultsContainer(queryUrl);
        }
    }

    @Override
    public SearchResultParser<Element> resolveParser() {
        return BeanUtils.getBean(GoogleSearchResultHtmlParser.class);
    }

    @Override
    public String prepareQueryUrl(SearchQuery query) {
        String googleSearchUrl = String.format(GOOGLE_SEARCH_URL_TEMPLATE, query.asString());
        String encodedGoogleSearchUrl = URLEncoder.encode(googleSearchUrl, StandardCharsets.UTF_8);
        String encodedUserAgent = URLEncoder.encode(searchProperties.getUserAgent(), StandardCharsets.UTF_8);
        return String.format(OPERIA_ENDPOINT_TEMPLATE, proxyProperties.getOperia().getKey(), encodedUserAgent, encodedGoogleSearchUrl);
    }
}
