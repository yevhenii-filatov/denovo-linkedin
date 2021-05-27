package com.dataox.linkedinscraper.service.validator;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
public class LinkedinValidatorException extends Exception{

    public LinkedinValidatorException(ValidatorMessage message){
        super(message.getMessage());
    }

    public LinkedinValidatorException(String message) {
        super(message);
    }
}
