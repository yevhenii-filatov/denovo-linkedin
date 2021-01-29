package com.dataox;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
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

    public static WebElement findElementBy(WebDriver webDriver, By by) {
        List<WebElement> elements = webDriver.findElements(by);
        return elements.isEmpty()
                ? null
                : elements.get(0);
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
        executeJavascript(webDriver, String.format("window.scrollBy(0, %d)", stepPx));
    }

    public static void scrollToElement(WebDriver webDriver, WebElement webElement, int topPanelSize) {
        int y = webElement.getLocation().getY() - topPanelSize;
        scrollTo(webDriver, y);
    }

    public static void scrollToElement(WebDriver webDriver, WebElement webElement) {
        int y = webElement.getLocation().getY();
        scrollTo(webDriver, y);
    }

    public static void scrollTo(WebDriver webDriver, int y) {
        int amountOfSteps = 10;
        int step = y / amountOfSteps;
        int counter = 0;
        for (int i = 0; i < amountOfSteps; i++) {
            WebDriverUtils.executeJavascript(webDriver, String.format("scrollTo(0,%d);", counter += step));
            CommonUtils.randomSleep(400, 800);
        }
    }

    public static String getElementHtml(WebElement webElement) {
        return webElement.getAttribute("outerHTML");
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
