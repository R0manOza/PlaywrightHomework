package ge.tbc.testautomation.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoWishListPage {
    private final Page page;
    private final Locator successMessage;
    private final Locator welcomeMessage;
    private final Locator mainWishlistGrid;

    public MagentoWishListPage(Page page) {
        this.page = page;
        this.successMessage = page.locator(".message-success:has-text('has been added')");
        this.welcomeMessage = page.locator("span.logged-in").first();
        this.mainWishlistGrid = page.locator("div.products-grid.wishlist");

    }

    public void verifyProductInWishList(String productName) {
        assertThat(mainWishlistGrid.locator(".product-item-name a:has-text('" + productName + "')")).isVisible();    }

    public Locator getSuccessMessage() {
        successMessage.waitFor();
        return successMessage.last();
    }

    public Locator getWelcomeMessage() {
        return welcomeMessage;
    }
}
