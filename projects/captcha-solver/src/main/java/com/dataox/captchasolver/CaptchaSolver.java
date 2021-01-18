package com.dataox.captchasolver;

import org.openqa.selenium.WebDriver;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

public interface CaptchaSolver {
    void solve(WebDriver webDriver);
}
