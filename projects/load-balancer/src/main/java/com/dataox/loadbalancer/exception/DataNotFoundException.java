package com.dataox.loadbalancer.exception;

/**
 * @author Dmitriy Lysko
 * @since 23/03/2021
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
