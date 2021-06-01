package com.dataox.linkedinscraper.service.validator;

import java.util.function.Supplier;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public class LinkedinValidatorException extends RuntimeException implements Supplier<LinkedinValidatorException> {

    public LinkedinValidatorException(ValidationType message){
        super(message.getMessage());
    }

    public LinkedinValidatorException(String message) {
        super(message);
    }

    public LinkedinValidatorException(){}

    @Override
    public LinkedinValidatorException get() {
        return new LinkedinValidatorException();
    }
}
