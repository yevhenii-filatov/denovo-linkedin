package com.dataox.linkedinscraper.scraping.service.error.detector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum LinkedinError implements Error {
    NO_ERRORS("", "", false),
    UNKNOWN_ERROR("20", "Unknown error", true),
    BANNED("21", "Account has been blocked", true),
    RESTRICTED("22", "Account has been temporarily restricted", true),
    EMAIL_VERIFICATION("23", "Need to pass email verification", true),
    PROFILE_IS_NOT_AVAILABLE("24", "Profile is not available", false),
    PAGE_NOT_FOUND("25", "Page not found", false),
    OOPS_ITS_NOT_YOU_ITS_US("26", "Unexpected error on Linkedin side", false),
    DONT_HAVE_ACCESS_TO_PROFILE("27", "The profiles of members who are outside your network have limited visibility", false),
    ISNT_QUITE_RIGHT("28","We noticed unusual activity from your account.",false),
    SOMETHING_WENT_WRONG("29","Something went wrong. Refresh the page",false);

    String code;
    String message;
    boolean critical;
}