package com.dataox.loadbalancer.exception;

/**
 * @author Dmitriy Lysko
 * @since 22/03/2021
 */
public class RecordNotFoundException extends RuntimeException{
    public RecordNotFoundException(String message) {
        super(message);
    }

}
