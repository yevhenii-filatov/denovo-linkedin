package com.dataox.linkedinscraper.scraping.service.error.detector;

import com.dataox.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LinkedinErrorDetector {
    private static final By UNEXPECTED_ERROR_ON_LINKEDIN_SIDE_SELECTOR = By.xpath("//h1[contains(text(),'Oops!')]");
    private static final By PROFILE_IS_NOT_AVAILABLE_HEADING_SELECTOR = By.cssSelector(".pv-profile-unavailable h1.artdeco-empty-state__headline");
    private static final By DONT_HAVE_ACCESS_TO_PROFILE = By.xpath("//h2[@id='out-of-network-modal-header'][contains(text(),'You don’t have access')]");
    private static final By COMPANY_PAGE_IS_NOT_AVAILABLE_HEADING_SELECTOR = By.xpath("//p[@class='artdeco-empty-state__message']");
    private static final By PAGE_NOT_FOUND_HEADING_SELECTOR = By.cssSelector(".not-found-404 .heading");
    private static final By UNEXPECTED_ERROR_TOAST_SELECTOR = By.xpath("//*[@id=\"app__container\"]/artdeco-toasts/artdeco-toast/div/p[contains(text(),'Something unexpected happened.')]");
    private static final By EMAIL_VERIFICATION_FORM_SELECTOR = By.xpath("//h2[contains(text(),'code we sent to your email')]");
    private static final By RESTRICTION_HEADING_SELECTOR = By.xpath("//h1[contains(text(),'restricted your account temporarily')]");
    private static final By BAN_HEADING_SELECTOR = By.xpath("//h1[contains(text(),'Your account has been restricted')]");
    private static final By ISNT_QUITE_RIGHT = By.xpath("//main/h1[text()='Something isn’t quite right']");
    private static final By SOMETHING_WENT_WRONG = By.xpath("//h1[text()='Something went wrong']");

    public LinkedinError detect(WebDriver webDriver) {
        if (banDetected(webDriver)) {
            return LinkedinError.BANNED;
        }
        if (restrictionDetected(webDriver)) {
            return LinkedinError.RESTRICTED;
        }
        if (emailVerificationDetected(webDriver)) {
            return LinkedinError.EMAIL_VERIFICATION;
        }
        if (unexpectedErrorToastDetected(webDriver)) {
            return LinkedinError.UNKNOWN_ERROR;
        }
        if (profileIsNotAvailable(webDriver)) {
            return LinkedinError.PROFILE_IS_NOT_AVAILABLE;
        }
        if (pageNotFound(webDriver)) {
            return LinkedinError.PAGE_NOT_FOUND;
        }
        if (unexpectedErrorOnLinkedinSideDetected(webDriver)) {
            return LinkedinError.OOPS_ITS_NOT_YOU_ITS_US;
        }
        if (dontHaveAccessToThisProfile(webDriver)) {
            return LinkedinError.DONT_HAVE_ACCESS_TO_PROFILE;
        }
        if (isntQuiteRight(webDriver)) {
            return LinkedinError.ISNT_QUITE_RIGHT;
        }
        if (somethingWentWrong(webDriver)) {
            return LinkedinError.SOMETHING_WENT_WRONG;
        }
        return LinkedinError.NO_ERRORS;
    }

    private boolean somethingWentWrong(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, SOMETHING_WENT_WRONG));
    }

    private boolean isntQuiteRight(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, ISNT_QUITE_RIGHT));
    }

    private boolean dontHaveAccessToThisProfile(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, DONT_HAVE_ACCESS_TO_PROFILE));
    }

    private boolean unexpectedErrorOnLinkedinSideDetected(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, UNEXPECTED_ERROR_ON_LINKEDIN_SIDE_SELECTOR));
    }

    private boolean profileIsNotAvailable(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, PROFILE_IS_NOT_AVAILABLE_HEADING_SELECTOR))
                || Objects.nonNull(WebDriverUtils.findElementBy(webDriver, COMPANY_PAGE_IS_NOT_AVAILABLE_HEADING_SELECTOR));
    }

    private boolean pageNotFound(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, PAGE_NOT_FOUND_HEADING_SELECTOR));
    }

    private boolean unexpectedErrorToastDetected(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, UNEXPECTED_ERROR_TOAST_SELECTOR));
    }

    private boolean emailVerificationDetected(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, EMAIL_VERIFICATION_FORM_SELECTOR));
    }

    private boolean restrictionDetected(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, RESTRICTION_HEADING_SELECTOR));
    }

    private boolean banDetected(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, BAN_HEADING_SELECTOR));
    }
}
