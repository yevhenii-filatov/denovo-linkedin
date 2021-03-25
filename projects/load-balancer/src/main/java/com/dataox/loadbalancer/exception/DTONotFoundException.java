package com.dataox.loadbalancer.exception;

/**
 * @author Dmitriy Lysko
 * @since 25/03/2021
 */
public class DTONotFoundException extends RuntimeException {
    public DTONotFoundException(String message) {
        super(message);
    }
}
