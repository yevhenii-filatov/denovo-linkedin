package com.dataoxx.scrapingutils.common;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

public final class JsonUtils {
    private JsonUtils() {
        throw new UnsupportedOperationException("utility class");
    }

    public static String findValueAsText(JsonNode container, String field) {
        JsonNode node = container.findValue(field);
        return Objects.nonNull(node) ? node.asText() : null;
    }

    public static Integer findValueAsInt(JsonNode container, String field) {
        JsonNode node = container.findValue(field);
        return Objects.nonNull(node) ? node.asInt() : -1;
    }
}
