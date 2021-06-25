package com.dataox.googleserp.util;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.util.UriEncoder;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author Dmitriy Lysko
 * @since 26/01/2021
 */
@UtilityClass
public final class NotificationUtils {

    private static final String NEW_LINE_CHARACTER = UriEncoder.encode("\n");

    public static String createErrorMessage(Throwable e) {
        return "Error occurred while starting search: ".concat(NEW_LINE_CHARACTER).concat(errorToString(e));
    }

    private static String errorToString(Throwable e) {
        String errorName = "Exception class name: " + e.getClass().getName();
        String errorMessage = "Exception message: " + e.getMessage();
        return String.join(NEW_LINE_CHARACTER, errorName, errorMessage);
    }

    public static String createSearchFailedMessage(Throwable e, List<Long> denovoIds) {
        String message = "Error occurred while performing search".concat(NEW_LINE_CHARACTER);
        message = message.concat(createErrorMessage(e)).concat(NEW_LINE_CHARACTER);
        message = message.concat("Failed denovoIds: ").concat(NEW_LINE_CHARACTER).concat(String.valueOf(denovoIds));
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
        String separator = NEW_LINE_CHARACTER + "------------------------" + NEW_LINE_CHARACTER;
        StringJoiner stringJoiner = new StringJoiner("");
        String messageTemplate = "%sException message: %s%n";
        for (Throwable exception : exceptions) {
            stringJoiner.add(String.format(messageTemplate, separator, exception.getMessage()));
        }
        return stringJoiner.toString();
    }

}
