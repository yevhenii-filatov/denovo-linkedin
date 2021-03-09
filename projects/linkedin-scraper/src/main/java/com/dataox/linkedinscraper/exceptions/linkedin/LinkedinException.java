package com.dataox.linkedinscraper.exceptions.linkedin;

/**
 * @author Dmitriy Lysko
 * @since 05/03/2021
 */

public abstract class LinkedinException extends RuntimeException {
    public LinkedinException(Throwable cause) {
        super(cause);
    }

    public LinkedinException(String message) {
        super(message);
    }

    public abstract String asString();
}
