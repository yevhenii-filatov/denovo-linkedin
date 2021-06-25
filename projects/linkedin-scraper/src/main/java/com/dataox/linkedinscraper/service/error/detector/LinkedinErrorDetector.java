package com.dataox.linkedinscraper.service.error.detector;

import com.dataox.WebDriverUtils;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Data
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
    private static final By LINKEDIN_LOGGED_OUT = By.xpath("//nav[@aria-label='Primary']//a[@class='nav__button-secondary'][text()='Sign in']");
    private static final By ADD_A_PHONE_NUMBER = By.xpath("//h2[text()='Add a phone number for security']");

    private final NotificationsService notificationsService;

    public LinkedinError detect(WebDriver webDriver) {
        if (banDetected(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin account has been banned.");
            return LinkedinError.BANNED;
        }
        if (restrictionDetected(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin account has been restricted.");
            return LinkedinError.RESTRICTED;
        }
        if (emailVerificationDetected(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin account requires email verification.");
            return LinkedinError.EMAIL_VERIFICATION;
        }
        if (unexpectedErrorToastDetected(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Unknown error.");
            return LinkedinError.UNKNOWN_ERROR;
        }
        if (profileIsNotAvailable(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Profile is not available.");
            return LinkedinError.PROFILE_IS_NOT_AVAILABLE;
        }
        if (pageNotFound(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Page was not found.");
            return LinkedinError.PAGE_NOT_FOUND;
        }
        if (unexpectedErrorOnLinkedinSideDetected(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Unexpected error on Linkedin side.");
            return LinkedinError.OOPS_ITS_NOT_YOU_ITS_US;
        }
        if (dontHaveAccessToThisProfile(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin account does not have access to the profile.");
            return LinkedinError.DONT_HAVE_ACCESS_TO_PROFILE;
        }
        if (isntQuiteRight(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Something isn’t quite right.");
            return LinkedinError.ISNT_QUITE_RIGHT;
        }
        if (somethingWentWrong(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Something went wrong.");
            return LinkedinError.SOMETHING_WENT_WRONG;
        }
        if (linkedinLoggedOut(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin account was logged out.");
            return LinkedinError.LOGGED_OUT;
        }
        if (addAPhoneNumber(webDriver)) {
            notificationsService.sendAll("LinkedinScraper: An error Has been occurred: Linkedin asks for a phone number for security.");
            return LinkedinError.ADD_A_PHONE_NUMBER;
        }
        return LinkedinError.NO_ERRORS;
    }

    private boolean addAPhoneNumber(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, ADD_A_PHONE_NUMBER));
    }

    private boolean linkedinLoggedOut(WebDriver webDriver) {
        return Objects.nonNull(WebDriverUtils.findElementBy(webDriver, LINKEDIN_LOGGED_OUT));
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
