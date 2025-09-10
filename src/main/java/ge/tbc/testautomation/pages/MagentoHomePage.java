package ge.tbc.testautomation.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoHomePage {
    private final Page page;
    private final Locator searchInput;
    private final Locator hotSellersItems;
    private final Locator miniCartIcon;
    private final Locator miniCartProductName;
    private final Locator miniCartProductPrice;
    private final Locator miniCartViewCartButton;
    public final Locator productReviewsSummary ;
    public final Locator productReviewLink ;
    public final Locator productReviewCountText ;
    public final Locator productItem;

    //--------- mobile test ---------------------
    // Desktop View Locators
    public final Locator headerPanel; // The whole top bar with Sign In/Create Account
    public final Locator headerSignInLink;
    public final Locator headerCreateAccountLink;
    public final Locator mainNavMenu; // The main navigation bar for desktop

    // Mobile View Locators
    public final Locator burgerMenuButton;
    public final Locator mobileMenuPanel;
    public final Locator signInLinkInMenu;
    public final Locator mobileMenuNavLinks;
    public final Locator mobileAccountMenu;
    public final Locator mobileMenu;


    //--------- ---------------------------------------------------------------
    public final Locator customerMenuDropdown;
    public final Locator signOutLink;
    public MagentoHomePage(Page page) {
        this.page = page;
        this.searchInput = page.locator("#search");
        this.hotSellersItems = page.locator(".product-items .product-item ");
        this.miniCartIcon = page.locator("a.showcart");
        this.miniCartProductName = page.locator("#mini-cart strong.product-item-name a").first();
        this.miniCartProductPrice = page.locator(".minicart-items .price");
        this.miniCartViewCartButton = page.getByText("View and Edit Cart");
        this.productReviewsSummary = page.locator(".product-reviews-summary");
        this.productReviewLink = page.locator(".reviews-actions a.action.view");
        this.productReviewCountText = page.locator(".reviews-actions a.action.view span");
        this.productItem = page.locator(".product-item");
        this.customerMenuDropdown = page.locator("li.customer-welcome button.action.switch");// ive tryed 100 different locators that all should work but they all just dont do anything !  why
        this.signOutLink = page.locator("li.customer-welcome.active a:has-text('Sign Out')");
        // Desktop
        this.headerPanel = page.locator(".panel.wrapper");
        this.headerSignInLink = this.headerPanel.locator("a:has-text('Sign In')");
        this.headerCreateAccountLink = this.headerPanel.locator("a:has-text('Create an Account')");
        this.mainNavMenu = page.locator("nav.navigation");

        // Mobile
        this.burgerMenuButton = page.locator(".nav-toggle");
        this.mobileMenuPanel = page.locator("#store\\.menu");
        this.signInLinkInMenu = page.locator("li.authorization-link a")
                .filter(new Locator.FilterOptions().setHasText("Sign In")).last();
        this.mobileMenuNavLinks = page.locator("nav.navigation");
        this.mobileAccountMenu = page.locator("a.nav-sections-item-switch:has-text('Account')");
        this.mobileMenu = page.locator("a.nav-sections-item-switch:has-text('Menu')");
    }

    public void searchForProduct(String productName) {
        searchInput.fill(productName);
        searchInput.press("Enter");
    }

    public List<Locator> getHotSellers() {
        hotSellersItems.first().waitFor();
        return hotSellersItems.all();
    }
    /**
     * Finds the color swatches WITHIN a specific product card.
     * @param productCard The Locator for a single .product-item.
     * @return A Locator for the color swatches for that product.
     */
    public Locator getColorSwatchesForProduct(Locator productCard) {
        return productCard.locator(".swatch-option.color");
    }
    /**
     * Gets the main product image element WITHIN a specific product card.
     * @param productCard The Locator for a single .product-item.
     * @return The Locator for the product's image.
     */
    public Locator getProductImage(Locator productCard) {
        return productCard.locator("img.product-image-photo");
    }
    public void selectRandomHotSeller(int index) {
        List<Locator> sellers = getHotSellers();
        sellers.get(index).click();
    }

    public String getProductNameFromMiniCart() {
        miniCartIcon.click();
        assertThat(miniCartProductName).isVisible();
        return miniCartProductName.textContent().trim();
    }

    public String getProductPriceFromMiniCart() {
        assertThat(miniCartProductPrice).isVisible();
        return miniCartProductPrice.textContent().trim();
    }

    public void goToCheckoutCart() {

        miniCartIcon.waitFor();
        miniCartIcon.click();
        miniCartViewCartButton.waitFor();
        miniCartViewCartButton.click();
    }
}
