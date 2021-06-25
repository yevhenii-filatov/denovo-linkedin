package com.dataox.linkedinscraper.exceptions.linkedin;

import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;

/**
 * @author Dmitriy Lysko
 * @since 05/03/2021
 */
public class LinkedinLoginException extends LinkedinException {
    private LinkedinError linkedinError;

    public LinkedinLoginException(String message, LinkedinError linkedinError) {
        super(message);
        this.linkedinError = linkedinError;
    }

    public LinkedinLoginException(Throwable cause) {
        super(cause);
    }

    public static LinkedinLoginException failedToLogin(String accountLogin, String accountPassword, LinkedinError linkedinError) {
        return new LinkedinLoginException(String.format("Failed to login into account with credentials %s %s",
                accountLogin,
                accountPassword), linkedinError);
    }

    @Override
    public String asString() {
        return String.format(
                "LinkedinError: %s%nException description: %s%n",
                Objects.nonNull(linkedinError) ? linkedinError.name() : StringUtils.EMPTY,
                ExceptionUtils.getStackTrace(this)
        );
    }
}
