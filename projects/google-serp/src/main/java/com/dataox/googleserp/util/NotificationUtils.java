package com.dataox.googleserp.util;

import org.yaml.snakeyaml.util.UriEncoder;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author Dmitriy Lysko
 * @since 26/01/2021
 */
public final class NotificationUtils {

    private static final String newLineCharacter = UriEncoder.encode("\n");

    public static String createErrorMessage(Throwable e) {
        String errorName = "Exception class name: " + e.getClass().getName();
        String errorMessage = "Exception message: " + e.getMessage();
        return String.join(newLineCharacter, errorName, errorMessage);
    }

    public static String createSearchFailedMessage(Throwable e, List<Long> denovoIds) {
        String message = "Error occurred while performing search".concat(newLineCharacter);
        message = message.concat(createErrorMessage(e)).concat(newLineCharacter);
        message = message.concat("Failed denovoIds: ").concat(newLineCharacter).concat(String.valueOf(denovoIds));
        return message;
    }

    public static String createSearchErrorMessage(Throwable e, String queryUrl) {
        return String.format("FAILED TO FETCH URL %s%nEXCEPTION TYPE: %s%nDESCRIPTION: %s%n",
                queryUrl,
                e.getClass().getName(),
                e.getMessage());
    }

    public static String createErrorMessage(List<Throwable> exceptions) {
        String messageTemplate = "Exceptions occurred inside tasks while performing search %n%s";
        return String.format(messageTemplate, createListOfExceptionMessages(exceptions));
    }

    private static String createListOfExceptionMessages(List<Throwable> exceptions) {
        String separator = newLineCharacter + "------------------------" + newLineCharacter;
        StringJoiner stringJoiner = new StringJoiner("");
        String messageTemplate = "%sException message: %s%n";
        for (Throwable exception : exceptions) {
            stringJoiner.add(String.format(messageTemplate, separator, exception.getMessage()));
        }
        return stringJoiner.toString();
    }

}
