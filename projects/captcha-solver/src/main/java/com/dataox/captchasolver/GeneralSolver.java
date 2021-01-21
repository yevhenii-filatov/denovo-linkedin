package com.dataox.captchasolver;

import com.dataox.captchasolver.detector.CaptchaType;
import com.dataox.captchasolver.detector.CaptchaTypeDetector;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@Primary
@RequiredArgsConstructor
public class GeneralSolver implements CaptchaSolver {
    private final CaptchaTypeDetector captchaTypeDetector;
    private final ArkoselabsFunCaptchaSolver arkoselabsFunCaptchaSolver;
    private final GoogleRecaptchaSolver googleRecaptchaSolver;

    @Override
    public void solve(WebDriver webDriver) {
        Document pageDocument = Jsoup.parse(webDriver.getPageSource(), webDriver.getCurrentUrl());
        CaptchaType type = captchaTypeDetector.detect(pageDocument);
        if (type == CaptchaType.NONE) {
            return;
        } else if ( type == CaptchaType.FUNCAPTCHA) {
            arkoselabsFunCaptchaSolver.solve(webDriver);
        } else  {
            googleRecaptchaSolver.solve(webDriver);
        }
    }
}
