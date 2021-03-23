package com.dataox.linkedinscraper.dto.types;

/**
 * @author Dmitriy Lysko
 * @since 02/02/2021
 */
public final class RecommendationType {
    public static final String RECEIVED = "RECEIVED";
    public static final String GIVEN = "GIVEN";

    private RecommendationType() {
        throw new UnsupportedOperationException("Constant class");
    }
}
