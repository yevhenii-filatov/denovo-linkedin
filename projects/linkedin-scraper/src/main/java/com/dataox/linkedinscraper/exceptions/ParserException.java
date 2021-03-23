package com.dataox.linkedinscraper.exceptions;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
public class ParserException extends RuntimeException{
    public ParserException(String message) {
        super(message);
    }

    public static ParserException exceptionOccurred(String exceptionName, String exceptionMessage, String profileUrl) {
        return new ParserException(String.format("Exception occurred while parsing profile %s%nException name: %s%nException message:%s",
                profileUrl,
                exceptionName,
                exceptionMessage));
    }
}
