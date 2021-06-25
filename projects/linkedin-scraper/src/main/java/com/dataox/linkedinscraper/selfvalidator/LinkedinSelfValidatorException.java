package com.dataox.linkedinscraper.selfvalidator;

import java.util.function.Supplier;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public class LinkedinSelfValidatorException extends RuntimeException implements Supplier<LinkedinSelfValidatorException> {

    public LinkedinSelfValidatorException(SelfValidationType message) {
        super(message.getMessage());
    }

    public LinkedinSelfValidatorException(String message) {
        super(message);
    }

    public LinkedinSelfValidatorException() {
    }

    @Override
    public LinkedinSelfValidatorException get() {
        return new LinkedinSelfValidatorException();
    }
}
