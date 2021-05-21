package com.dataox;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.ScrollingDirection.*;

/**
 * @author Yevhenii Filatov
 * @since 11/26/20
 */
@Slf4j
public final class WebDriverUtils {

    private static Long lastScrollYInElementPosition;

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

    public static Optional<WebElement> findWebElementBy(WebDriver webDriver, By by) {
        List<WebElement> elements = webDriver.findElements(by);
        return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
    }

    public static WebElement findElementBy(WebDriver webDriver, By by) {
        List<WebElement> elements = webDriver.findElements(by);
        return elements.isEmpty()
                ? null
                : elements.get(0);
    }

    public static Optional<WebElement> findWebElementFromParentBy(WebElement parent, By by) {
        WebElement element;
        try {
            element = parent.findElement(by);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
        return Optional.of(element);
    }

    public static void enterDataIntoTextFieldWithKeyboard(WebElement field, String data) {
        for (char symbol : data.toCharArray()) {
            field.sendKeys(String.valueOf(symbol));
            randomSleep(100, 500);
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
        ScrollingDirection direction = random();
        long step = CommonUtils.randomLong(250, 400);
        scroll(webDriver, direction, step);
    }

    public static void scroll(WebDriver webDriver, ScrollingDirection direction, long stepPx) {
        if (direction == UP) {
            stepPx *= -1;
        }
        executeJavascript(webDriver, String.format("window.scrollBy(0, %d)", stepPx));
    }

    public static void scrollToAndClickOnElement(WebDriver webDriver, Actions actions, WebElement webElement) {
        scrollToAndClickOnElement(webDriver, actions, webElement, CommonUtils.randomLong(750, 1500), 400);
    }

    public static void scrollToAndClickOnElement(WebDriver webDriver,
                                                 Actions actions,
                                                 WebElement webElement,
                                                 Long clickPause,
                                                 int topPanelHeight) {
        scrollToElement(webDriver, webElement, topPanelHeight);
        clickOnElement(webElement, actions, clickPause);
    }

    public static void scrollToElement(WebDriver webDriver, WebElement webElement, int topPanelHeight) {
        int elementY = webElement.getLocation().getY();
        elementY -= topPanelHeight;
        scrollTo(webDriver, elementY);
    }

    public static void scrollToElement(WebDriver webDriver, WebElement webElement) {
        int y = webElement.getLocation().getY();
        scrollTo(webDriver, y);
    }

    public static void scrollTo(WebDriver webDriver, int desiredScrollY) {
        int amountOfSteps = 30;
        Long currentScrollY = getScrollY(webDriver);
        int step = (int) (Math.abs((desiredScrollY - currentScrollY)) / amountOfSteps);
        ScrollingDirection scrollingDirection = currentScrollY > desiredScrollY ? UP : DOWN;
        for (int i = 0; i < amountOfSteps; i++) {
            scroll(webDriver, scrollingDirection, step);
            randomSleep(75, 125);
        }
    }

    public static Long getScrollY(WebDriver webDriver) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Long lng = 0L;
        try {
            lng = (Long) js.executeScript("return scrollY");
            lastScrollYInElementPosition = lng;
        } catch (ClassCastException e) {
            return lastScrollYInElementPosition;
        }
        return lng;
    }

    public static void scrollToTheBottomOfElement(WebDriver webDriver, WebElement scrollingElement, int scrollStop) {
        int desiredScrollY = 0;
        Long beforeScroll;
        Long afterScroll;
        do {
            beforeScroll = getCurrentScrollYInElement(webDriver, scrollingElement);
            executeJavascript(webDriver, "arguments[0].scrollTop=arguments[1]", scrollingElement, desiredScrollY += scrollStop);
            afterScroll = getCurrentScrollYInElement(webDriver, scrollingElement);
            randomSleep(1500, 2500);
        } while (!beforeScroll.equals(afterScroll));
    }

    public static Long getCurrentScrollYInElement(WebDriver webDriver, WebElement scrollingElement) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Long lng = 0L;
        try {
            lng = (Long) js.executeScript("return arguments[0].scrollTop", scrollingElement);
            lastScrollYInElementPosition = lng;
        } catch (ClassCastException e) {
            return lastScrollYInElementPosition;
        }
        return lng;
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

    public static void clickOnElement(WebElement elementToClick, Actions actions, Long pause) {
        actions.moveToElement(elementToClick).pause(pause).click().perform();
    }

    public static void clickOnElement(WebElement elementToClick, Actions actions) {
        actions.moveToElement(elementToClick).pause(CommonUtils.randomLong(750, 1500)).click().perform();
    }

    public enum ScrollingDirection {
        UP, DOWN;

        public static ScrollingDirection random() {
            long number = CommonUtils.randomLong(1000, 5000);
            return number > 2500 ? UP : DOWN;
        }
    }
}
