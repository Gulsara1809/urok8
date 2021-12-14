package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.SearchPageObject;

public class iOSSearchPageObject extends SearchPageObject
{
    static {
        SEARCH_INIT_ELEMENT = "xpath://XCUIElementTypeSearchField[@name='Search Wikipedia']";
        SEARCH_INPUT = "xpath://XCUIElementTypeSearchField[@value='Search Wikipedia']";
        SEARCH_CANCEL_BUTTON =" id:Close";
        SEARCH_RESULT_BY_SUBSTRING_TPL = "xpath://*[@resource-id ='org.wikipedia:id/page_list_item_container']//*[@text= '{SUBSTRING}']";
        SEARCH_RESULT_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']";
        SEARCH_EMPTY_RESULT_ELEMENT = "xpath://XCUIElementTypeStaticText[@name='No results found']";
    }

    public  iOSSearchPageObject(AppiumDriver driver)
    {
        super(driver);
    }
}
