package com.dataox.linkedinscraper.exceptions.linkedin;

import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;

/**
 * @author Dmitriy Lysko
 * @since 05/03/2021
 */

public class LinkedinScrapingException extends LinkedinException {
    @Setter
    private LinkedinError linkedinError;

    public LinkedinScrapingException(Throwable cause) {
        super(cause);
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
