package ge.tbc.testautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MagentoSuccessPage {
    private final Page page;

    public final Locator pageTitle;
    public final Locator orderNumber;
    public final Locator continueShoppingButton;

    public MagentoSuccessPage(Page page) {
        this.page = page;
        this.pageTitle = page.locator(".base:has-text('Thank you for your purchase!')");
        this.orderNumber = page.locator(".order-number strong");
        this.continueShoppingButton = page.locator("a.action.primary.continue");
    }
}