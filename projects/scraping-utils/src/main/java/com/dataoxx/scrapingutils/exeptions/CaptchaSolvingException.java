package com.dataoxx.scrapingutils.exeptions;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

public class CaptchaSolvingException extends RuntimeException {
    public CaptchaSolvingException(String message) {
        super(message);
    }

    public static CaptchaSolvingException notFound(String what) {
        return new CaptchaSolvingException(String.format("Not found: %s%n", what));
    }
}
