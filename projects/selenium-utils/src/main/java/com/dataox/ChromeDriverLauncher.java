package com.dataox;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author Yevhenii Filatov
 * @since 11/26/20
 */

@Getter
public class ChromeDriverLauncher implements AutoCloseable {
    private final WebDriver webDriver;

    public ChromeDriverLauncher(ChromeOptions options) {
        this.webDriver = new ChromeDriver(options);
    }

    @Override
    public void close() {
        webDriver.getWindowHandles().forEach(windowHandle -> closeWindow(webDriver, windowHandle));
        webDriver.quit();
    }

    private void closeWindow(WebDriver webDriver, String windowHandle) {
        webDriver.switchTo().window(windowHandle);
        webDriver.close();
    }
}
