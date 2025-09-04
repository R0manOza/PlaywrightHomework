package ge.tbc.testautomation.tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static Page page;
    protected static BrowserContext context;
    @BeforeClass
    public static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // Context and page are created once
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://magento.softwaretestingboard.com/");
    }

    @AfterClass
    public static void tearDownClass() {
        // Close the shared context and browser
        context.close();
        browser.close();
        playwright.close();
    }


}
