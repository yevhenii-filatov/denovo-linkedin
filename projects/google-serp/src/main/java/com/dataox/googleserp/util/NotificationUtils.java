package com.dataox.googleserp.util;

/**
 * @author Dmitriy Lysko
 * @since 26/01/2021
 */
public final class NotificationUtils {

    public static String createErrorMessage(Throwable e) {
        String message = "Error occurred: ";
        String errorName = "Exception class name: " + e.getClass().getName();
        String errorMessage = "Exception message: " + e.getMessage();
        return String.join("\n", message, errorName, errorMessage);
    }
}
