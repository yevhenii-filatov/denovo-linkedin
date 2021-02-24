package com.dataox.linkedinscraper.parser.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class ParsingUtils {

    public static Element toElement(String source) {
        return Jsoup.parse(source, "https://www.linkedin.com").body();
    }

}
