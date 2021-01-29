package com.dataox.linkedinscraper.scraping.service.login;

import com.dataox.captchasolver.CaptchaSolver;
import com.dataox.linkedinscraper.dto.AccountCredentials;
import com.dataox.linkedinscraper.scraping.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.scraping.service.error.detector.LinkedinErrorDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.dataox.WebDriverUtils.*;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountCredentials accountCredentials;
    private final CaptchaSolver captchaSolver;
    private final LinkedinErrorDetector errorDetector;
    private static final By LOGIN_INPUT_FIELD = By.xpath("//input[@autocomplete='username']");
    private static final By PASSWORD_INPUT_FIELD = By.xpath("//input[@autocomplete='current-password']");
    private static final By SUBMIT_BUTTON = By.xpath("//button[@class='sign-in-form__submit-button'][@type='submit']");

    public void performLogin(WebDriver webDriver) {
        log.info("Performing login to linkedin with user credentials: {}", accountCredentials);
        fillFieldsAndSubmitForm(webDriver);
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (linkedinError.isCritical()) {
            //send internal error notification
            log.error("Can't login into account. Error occurred {}",linkedinError);
        }
        captchaSolver.solve(webDriver);
        System.out.println("d");
    }

    private void fillFieldsAndSubmitForm(WebDriver webDriver) {
        WebElement loginInputField = webDriver.findElement(LOGIN_INPUT_FIELD);
        WebElement passwordInputField = webDriver.findElement(PASSWORD_INPUT_FIELD);
        enterDataIntoTextFieldWithKeyboard(loginInputField,accountCredentials.getLogin());
        enterDataIntoTextFieldWithKeyboard(passwordInputField,accountCredentials.getPassword());
        webDriver.findElement(SUBMIT_BUTTON).click();
    }
}
