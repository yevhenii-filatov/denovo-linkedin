package com.dataox.linkedinscraper.scraping.exceptions;

/**
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    public static ElementNotFoundException create(String what) {
        return new ElementNotFoundException(String.format("Not found: %s%n", what));
    }
}
