package com.dataox.linkedinscraper.scraping.exceptions;

import org.openqa.selenium.NotFoundException;

/**
 * @author Dmitriy Lysko
 * @since 23/02/2021
 */
public class ElementNotFoundException extends NotFoundException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    public static ElementNotFoundException notFound(String what) {
        return new ElementNotFoundException(String.format("Not found: %s%n", what));
    }
}
