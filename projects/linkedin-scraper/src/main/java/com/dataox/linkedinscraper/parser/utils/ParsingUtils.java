package com.dataox.linkedinscraper.parser.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParsingUtils {

    public static Element toElement(String source) {
        return Jsoup.parse(source, "https://www.linkedin.com").body();
    }
}
