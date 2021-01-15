package com.dataox.scrapingutils.selenium;

import com.dataox.scrapingutils.common.CommonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;

/**
 * @author Yevhenii Filatov
 * @since 11/26/20
 */

public final class WebDriverUtils {
    private WebDriverUtils() {
        throw new UnsupportedOperationException("utility class");
    }

    public static Optional<WebElement> findClickableBy(WebDriver webDriver, By locator, long timeoutSec) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeoutSec);
        try {
            return Optional.of(wait.until(ExpectedConditions.elementToBeClickable(locator)));
        } catch (TimeoutException e) {
            return Optional.empty();
        }
    }

    public static void enterDataIntoTextFieldWithKeyboard(WebElement field, String data) {
        for (char symbol : data.toCharArray()) {
            field.sendKeys(String.valueOf(symbol));
            CommonUtils.randomSleep(100, 500);
        }
    }

    public static void executeJavascript(WebDriver webDriver, String script, Object... args) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript(script, args);
    }

    public static void showHiddenElement(WebDriver webDriver, String cssSelector) {
        changeElementVisibility(webDriver, cssSelector, true);
    }

    public static void hideElement(WebDriver webDriver, String cssSelector) {
        changeElementVisibility(webDriver, cssSelector, false);
    }

    private static void changeElementVisibility(WebDriver webDriver, String cssSelector, boolean visible) {
        String display = visible ? "block" : "none";
        String script = String.format("document.querySelector('%s').style='display: %s;'", cssSelector, display);
        executeJavascript(webDriver, script);
    }

    public static void randomScroll(WebDriver webDriver) {
        ScrollingDirection direction = ScrollingDirection.random();
        long step = CommonUtils.randomLong(250, 400);
        scroll(webDriver, direction, step);
    }

    public static void scroll(WebDriver webDriver, ScrollingDirection direction, long stepPx) {
        if (direction == ScrollingDirection.UP) {
            stepPx *= -1;
        }
        executeJavascript(webDriver, String.format("window.scrollBy(0, %d)", stepPx), ArrayUtils.EMPTY_OBJECT_ARRAY);
    }

    public static void removeAttribute(WebDriver webDriver, String cssSelector, String attribute) {
        String script = String.format("document.querySelector('%s').removeAttribute('%s');", cssSelector, attribute);
        executeJavascript(webDriver, script);
    }

    public static void setAttribute(WebDriver webDriver, String cssSelector, String attribute, String value) {
        String script = String.format("document.querySelector('%s').setAttribute('%s', '%s')", cssSelector, attribute, value);
        executeJavascript(webDriver, script);
    }

    public static void deleteDataFromTextFieldWithKeyboard(WebElement element) {
        element.sendKeys(Keys.CONTROL, "a");
        element.sendKeys(Keys.DELETE);
    }

    public enum ScrollingDirection {
        UP, DOWN;

        public static ScrollingDirection random() {
            long number = CommonUtils.randomLong(1000, 5000);
            return number > 2500 ? UP : DOWN;
        }
    }
}
