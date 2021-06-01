package com.dataox.linkedinscraper.scraping.service.login;

import com.dataox.captchasolver.CaptchaSolver;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinLoginException;
import com.dataox.linkedinscraper.scraping.configuration.property.LinkedinProperties;
import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.WebDriverUtils.*;
import static com.dataox.linkedinscraper.scraping.service.emailVerificator.EmailVerification.verifyEmail;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginService {

    LinkedinProperties linkedinProperties;
    CaptchaSolver captchaSolver;
    LinkedinErrorDetector errorDetector;
    static String LINKEDIN_LOGIN_PAGE_URL = "https://www.linkedin.com/";
    static String CANT_LOGIN_MESSAGE = "Can't login into account.";
    static By LOGIN_INPUT_FIELD = By.xpath("//input[@autocomplete='username']");
    static By PASSWORD_INPUT_FIELD = By.xpath("//input[@autocomplete='current-password']");
    static By SUBMIT_BUTTON = By.xpath("//button[@class='sign-in-form__submit-button'][@type='submit']");
    static By PROFILE_SECTION = By.xpath("//div[contains(@class,'artdeco-card overflow-hidden')]");
    static By CAPTCHA_FORM = By.cssSelector("form#captcha-challenge");

    public void performLogin(WebDriver webDriver) {
        try {
            webDriver.get(LINKEDIN_LOGIN_PAGE_URL);
            log.info("Performing login to linkedin with user credentials: Login:{} Password:{}",
                    linkedinProperties.getProfileLogin(),
                    linkedinProperties.getProfilePassword());
            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            fillFieldsAndSubmitForm(webDriver);
            solveCaptcha(webDriver);
            checkForErrors(webDriver);
            wait.until(ExpectedConditions.presenceOfElementLocated(PROFILE_SECTION));
        } catch (Exception e) {
            throw new LinkedinLoginException(e);
        }
    }

    private void checkForErrors(WebDriver webDriver) {
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (linkedinError == LinkedinError.EMAIL_VERIFICATION) {
            verifyEmail(webDriver);
        } else if (linkedinError != LinkedinError.NO_ERRORS) {
            log.error(CANT_LOGIN_MESSAGE + " Error occurred {}", linkedinError);
            throw LinkedinLoginException.failedToLogin(linkedinProperties.getProfileLogin(), linkedinProperties.getProfilePassword(), linkedinError);
        }
    }

    private void solveCaptcha(WebDriver webDriver) {
        captchaSolver.solve(webDriver);
        WebElement captchaForm = findElementBy(webDriver, CAPTCHA_FORM);
        if (nonNull(captchaForm))
            captchaForm.submit();
    }

    private void fillFieldsAndSubmitForm(WebDriver webDriver) {
        WebElement loginInputField = findWebElementBy(webDriver, LOGIN_INPUT_FIELD)
                .orElseThrow(() -> ElementNotFoundException.create("Login input field"));
        WebElement passwordInputField = findWebElementBy(webDriver, PASSWORD_INPUT_FIELD)
                .orElseThrow(() -> ElementNotFoundException.create("Password input field"));
        WebElement submitButton = findWebElementBy(webDriver, SUBMIT_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.create("Submit login form button"));
        enterDataIntoTextFieldWithKeyboard(loginInputField, linkedinProperties.getProfileLogin());
        enterDataIntoTextFieldWithKeyboard(passwordInputField, linkedinProperties.getProfilePassword());
        clickOnElement(submitButton, new Actions(webDriver), randomLong(1500, 2500));
    }
}
