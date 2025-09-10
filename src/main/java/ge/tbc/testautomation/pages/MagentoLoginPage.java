package ge.tbc.testautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MagentoLoginPage {
    private final Page page;

    public final Locator emailInput;
    public final Locator passwordInput;
    public final Locator signInButton;

    public MagentoLoginPage(Page page) {
        this.page = page;
        this.emailInput = page.locator("#email");
        this.passwordInput = page.locator("input[name='login[password]']");
        this.signInButton = page.locator("button.action.login.primary");
    }
}