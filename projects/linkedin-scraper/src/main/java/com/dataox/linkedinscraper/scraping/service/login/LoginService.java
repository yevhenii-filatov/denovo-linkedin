package com.dataox.linkedinscraper.scraping.service.login;

import com.dataox.captchasolver.CaptchaSolver;
import com.dataox.linkedinscraper.scraping.configuration.property.LinkedinProperties;
import com.dataox.linkedinscraper.scraping.exceptions.LinkedinException;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import com.dataox.notificationservice.service.TelegramNotificationsServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LinkedinProperties linkedinProperties;
    private final CaptchaSolver captchaSolver;
    private final LinkedinErrorDetector errorDetector;
    private final TelegramNotificationsServiceProvider notificationsService;
    private final ApplicationContext applicationContext;
    private static final String LINKEDIN_LOGIN_PAGE_URL = "https://www.linkedin.com/";
    private static final String CANT_LOGIN_MESSAGE = "Can't login into account.";
    private static final By LOGIN_INPUT_FIELD = By.xpath("//input[@autocomplete='username']");
    private static final By PASSWORD_INPUT_FIELD = By.xpath("//input[@autocomplete='current-password']");
    private static final By SUBMIT_BUTTON = By.xpath("//button[@class='sign-in-form__submit-button'][@type='submit']");
    private static final By PROFILE_SECTION = By.xpath("//div[contains(@class,'artdeco-card overflow-hidden')]");
    private static final By CAPTCHA_FORM = By.cssSelector("form#captcha-challenge");

    public void performLogin(WebDriver webDriver) {
        webDriver.get(LINKEDIN_LOGIN_PAGE_URL);
        log.info("Performing login to linkedin with user credentials: {} {}",
                linkedinProperties.getProfileLogin(),
                linkedinProperties.getProfilePassword());
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        fillFieldsAndSubmitForm(webDriver);
        checkForErrors(webDriver);
        solveCaptcha(webDriver);
        checkForErrors(webDriver);
        wait.until(ExpectedConditions.presenceOfElementLocated(PROFILE_SECTION));
    }

    private void checkForErrors(WebDriver webDriver) {
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (linkedinError != LinkedinError.NO_ERRORS) {
            notificationsService.send(String.format("Scraper name: %s%n%s", applicationContext.getApplicationName(), CANT_LOGIN_MESSAGE));
            log.error(CANT_LOGIN_MESSAGE + " Error occurred {}", linkedinError);
            throw new LinkedinException(CANT_LOGIN_MESSAGE);
        }
    }

    private void solveCaptcha(WebDriver webDriver) {
        captchaSolver.solve(webDriver);
        WebElement captchaForm = findElementBy(webDriver, CAPTCHA_FORM);
        if (nonNull(captchaForm))
            captchaForm.submit();
    }

    private void fillFieldsAndSubmitForm(WebDriver webDriver) {
        WebElement loginInputField = webDriver.findElement(LOGIN_INPUT_FIELD);
        WebElement passwordInputField = webDriver.findElement(PASSWORD_INPUT_FIELD);
        enterDataIntoTextFieldWithKeyboard(loginInputField, linkedinProperties.getProfileLogin());
        enterDataIntoTextFieldWithKeyboard(passwordInputField, linkedinProperties.getProfilePassword());
        clickOnElement(webDriver.findElement(SUBMIT_BUTTON), new Actions(webDriver), randomLong(1500, 2500));
    }
}
