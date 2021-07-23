package com.dataox.googleserp.exceptions;

/**
 * @author Dmitriy Lysko
 * @since 26/01/2021
 */
public class SearchException extends RuntimeException {
    public SearchException(String message) {
        super(message);
    }

    public static SearchException create(Exception e) {
        String exceptionClass = e.getClass().getName();
        String exceptionMessage = e.getMessage();
        String errorMessage = String.format("SEARCH FAILED%nERROR TYPE: %s%nMESSAGE: %s%n", exceptionClass, exceptionMessage);
        return new SearchException(errorMessage);
    }
}

