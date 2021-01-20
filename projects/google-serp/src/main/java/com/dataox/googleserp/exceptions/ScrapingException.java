package com.dataox.googleserp.exceptions;

/**
 * @author Yevhenii Filatov
 * @since 1/12/21
 */

public class ScrapingException extends RuntimeException {
    public ScrapingException(String message) {
        super(message);
    }

    public static ScrapingException create(Exception e) {
        String exceptionClass = e.getClass().getName();
        String exceptionMessage = e.getMessage();
        String errorMessage = String.format("SCRAPING FAILED%nERROR TYPE: %s%nMESSAGE: %s%n", exceptionClass, exceptionMessage);
        return new ScrapingException(errorMessage);
    }
}
