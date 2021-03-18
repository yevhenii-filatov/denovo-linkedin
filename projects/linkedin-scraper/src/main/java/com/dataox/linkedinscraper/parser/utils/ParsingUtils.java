package com.dataox.linkedinscraper.parser.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParsingUtils {

    public static Element toElement(String source) {
        return Jsoup.parse(source, "https://www.linkedin.com").body();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static long toLong(String numbers) {
        return Long.parseLong(numbers);
    }

    public static String extractNumbers(String text) {
        return normalizeSpace(text.replaceAll("\\D+", ""));
    }

    public static String extractLetters(String text) {
        return normalizeSpace(text.replaceAll("\\d+|r+", ""));
    }

    public static String capitalizeFirstLetter(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
