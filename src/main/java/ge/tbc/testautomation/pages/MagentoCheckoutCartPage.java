package ge.tbc.testautomation.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
public class MagentoCheckoutCartPage {
    private final Page page;
    private final Locator deleteItemButton;
    private final Locator emptyCartMessage;

    public MagentoCheckoutCartPage(Page page) {
        this.page = page;
        this.deleteItemButton = page.locator("a.action.action-delete").first();
        this.emptyCartMessage = page.locator(".cart-empty");
    }

    public void deleteFirstItem() {
        deleteItemButton.waitFor();
        deleteItemButton.click();
    }

    public Locator getEmptyCartMessage() {
        return emptyCartMessage;
    }
}
