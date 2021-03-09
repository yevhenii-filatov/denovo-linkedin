package com.dataox.linkedinscraper.exceptions.linkedin;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Dmitriy Lysko
 * @since 05/03/2021
 */
public class LinkedinParsingException extends LinkedinException {
    public LinkedinParsingException(Throwable cause) {
        super(cause);
    }

    @Override
    public String asString() {
        return ExceptionUtils.getStackTrace(this);
    }
}
