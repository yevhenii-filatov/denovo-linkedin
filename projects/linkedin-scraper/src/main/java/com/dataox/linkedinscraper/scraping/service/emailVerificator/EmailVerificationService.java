package com.dataox.linkedinscraper.scraping.service.emailVerificator;

import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.WebDriverUtils.*;

/**
 * @author Viacheslav_Yakovenko
 * @since 31.05.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    static By EMAIL_VERIFICATION_FIELD = By.xpath("//*[@id=\"input__email_verification_pin\"]");
    static By SUBMIT_BUTTON = By.xpath("//*[@id=\"email-pin-submit-button\"]");
    private final NotificationsService notificationsService;

    public void verifyEmail(WebDriver webDriver) throws IOException, InterruptedException {
        File verificationFile = new File("./verificationCode.txt");
        Scanner sc = new Scanner(verificationFile);
        notificationsService.sendAll("LinkedinScraper: waiting 120 seconds for verification code to be entered.");
        Thread.sleep(120000);
        String code = sc.nextLine();
        log.info("Verification code used: {}", code);
        notificationsService.sendAll("Verification code used: ".concat(code));
        WebElement emailVerificationField = findWebElementBy(webDriver, EMAIL_VERIFICATION_FIELD)
                .orElseThrow(() -> ElementNotFoundException.create("Email verification field"));
        WebElement submitButton = findWebElementBy(webDriver, SUBMIT_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.create("Submit button field"));
        enterDataIntoTextFieldWithKeyboard(emailVerificationField, code);
        clickOnElement(submitButton, new Actions(webDriver), randomLong(1500, 2500));
    }

}
