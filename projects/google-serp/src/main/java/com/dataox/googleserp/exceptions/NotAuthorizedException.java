package com.dataox.googleserp.exceptions;

/**
 * @author Viacheslav_Yakovenko
 * @since 18.06.2021
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
