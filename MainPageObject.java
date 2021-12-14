package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import lib.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;
import java.util.regex.Pattern;

public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }


    public WebElement waitForElementPresent(String locator, String error_message, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );

    }

    public WebElement waitForElementPresent1(String locator, String error_message, long timeoutInSeconds) {
        WebElement element = this.waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public WebElement waitForElementAndClick(String locator, String error_message, long timeoutInSeconds) {
        WebElement element = this.waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndClear(String locator, String error_message, long timeoutInSeconds) {
        WebElement element = this.waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public boolean waitForElementNotPresent(String locator, String error_message, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        return wait.withMessage(error_message + "\n").until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeoutInSeconds) {
        WebElement element = this.waitForElementAndClear(locator, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }


    public int getAmountOfElements(String locator) {

        By by = this.getLocatorByString(locator);
        List elements = driver.findElements(by);
        return elements.size();
    }


    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);
        action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
    }

    protected void swipeUpQuick() {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(String locator, String error_message, int max_swipes) {
        By by = this.getLocatorByString(locator);
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {
            if (already_swiped > max_swipes) {
                waitForElementPresent(locator, "Cannot find element by swiping up", 0);
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void swipeUPTiLLElementAppear(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (!this.isElementLocatedOnTheScreen(locator)) {
            if (already_swiped > max_swipes) {
                Assert.assertTrue(error_message, this.isElementLocatedOnTheScreen(locator));
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public boolean isElementLocatedOnTheScreen(String locator) {
        int element_locator_by_y = this.waitForElementPresent(locator, "Cannot find element by locator", 1).getLocation().getY();

        int screen_size_by_y = driver.manage().window().getSize().getHeight();
        return element_locator_by_y < screen_size_by_y;

    }

    public void clickElementToTheRightUpperCorner(String locator, String error_message) {

            WebElement element = this.waitForElementPresent(locator + "/..", error_message);
        int right_x = element.getLocation().getX();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;
        int width = element.getSize().getWidth();

        int point_to_click_x = (right_x + width) - 3;
        int point_to_click_y = middle_y;

        TouchAction action = new TouchAction(driver);
        action.tap(point_to_click_x, point_to_click_y).perform();
    }



    public void swipeElementToLeft(String locator, String error_message) {
        WebElement element = waitForElementPresent(locator,
                error_message,
                10);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;
        TouchAction action = new TouchAction((AppiumDriver) driver);
        action.press (right_x, middle_y);
        action.waitAction(300);
                if (Platform.getInstance().isAndroid()){
                    action.moveTo(left_x, middle_y);
                }else {
                    int offset_x = (-1 * element.getSize().getWidth());
                    action.moveTo(offset_x, 0);
                }

                action.release();
                action.perform();

    }

    public void assertElementNotPresent(String locator, String error_message) {
        int amount_of_element = getAmountOfElements(locator);
        if (amount_of_element > 0) {
            String default_message = "An element '" + locator + "' supposed to be present";
            throw new AssertionError(default_message + "" + error_message);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeoutInSeconds) {


    WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
return element.getAttribute(attribute);
}
     private By getLocatorByString(String locator_with_type)
     {
         String[] exploded_locator = locator_with_type.split(Pattern.quote(":"),2);
         String by_type = exploded_locator [0];
         String locator = exploded_locator [1];
         if (by_type.equals("xpath")) {
             return By.id(locator);
         } else if (by_type.equals("id")) {
             return By.id(locator);
         } else {
             throw new IllegalArgumentException("Cannot get typ of locator. Locator: " +locator_with_type);
         }
     }
}


