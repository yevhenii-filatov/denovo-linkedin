package com.dataox.googleserp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yevhenii Filatov
 * @since 5/14/20
 **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GoogleSearchParameters {
    private static final Map<String, String> GOOGLE_SEARCH_PARAMS;

    static {
        GOOGLE_SEARCH_PARAMS = new HashMap<>();
        GOOGLE_SEARCH_PARAMS.put("site", "site:%s");
        GOOGLE_SEARCH_PARAMS.put("intitle", "intitle:%s");
        GOOGLE_SEARCH_PARAMS.put("allintitle", "intitle:%s");
        GOOGLE_SEARCH_PARAMS.put("intext", "intext:%s");
        GOOGLE_SEARCH_PARAMS.put("allintext", "intext:%s");
        GOOGLE_SEARCH_PARAMS.put("exactly", "\"%s\"");
        GOOGLE_SEARCH_PARAMS.put("inurl", "inurl:%s");
        GOOGLE_SEARCH_PARAMS.put("allinurl", "allinurl:%s");
    }

    public static String prepareParameter(String name, String... values) {
        String template = GOOGLE_SEARCH_PARAMS.get(name.toLowerCase());
        if (Objects.isNull(template)) return null;
        String joinedValues = normalizeParameters(values).stream()
                .flatMap(GoogleSearchParameters::splitIntoWordsAndCreateStream)
                .collect(Collectors.joining("+"));
        return String.format(template, joinedValues);
    }

    private static List<String> normalizeParameters(String... values) {
        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank)
                .map(GoogleSearchParameters::removeCommas)
                .map(GoogleSearchParameters::removeAmpersands)
                .map(StringUtils::normalizeSpace)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private static String removeAmpersands(String word) {
        return RegExUtils.replaceAll(word, "&", StringUtils.EMPTY);
    }

    private static Stream<String> splitIntoWordsAndCreateStream(String string) {
        return Arrays.stream(StringUtils.split(string, StringUtils.SPACE))
                .flatMap(word -> Arrays.stream(StringUtils.split(word, "-")));
    }

    private static String removeCommas(String string) {
        return StringUtils.replace(string, ", ", StringUtils.EMPTY);
    }
}
