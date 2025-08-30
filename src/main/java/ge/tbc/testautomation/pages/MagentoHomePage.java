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

    public MagentoHomePage(Page page) {
        this.page = page;
        this.searchInput = page.locator("#search");
        this.hotSellersItems = page.locator(".product-items .product-item ");
        this.miniCartIcon = page.locator("a.showcart");
        this.miniCartProductName = page.locator("#mini-cart strong.product-item-name a").first();
        this.miniCartProductPrice = page.locator(".minicart-items .price");
        this.miniCartViewCartButton = page.getByText("View and Edit Cart");
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
