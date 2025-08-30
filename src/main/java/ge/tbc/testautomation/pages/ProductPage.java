package ge.tbc.testautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.Random;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPage {
    private final Page page;
    private final Locator productName;
    private final Locator productPrice;
    private final Locator colorSwatches;
    private final Locator sizeSwatches;
    private final Locator addToCartButton;
    private final Locator addToWishListButton;
    private final Locator mainImage;
    private final Locator stockStatus;
    private final Locator outOfStockMessage;
    private final Locator miniCartIcon;
    private final Locator miniCartProductName;
    private final Locator miniCartProductPrice;
    private final Locator miniCartViewCartButton;
    public ProductPage(Page page) {
        this.page = page;
        this.productName = page.locator("h1.page-title");
        this.productPrice = page.locator(".product-info-main .price");
        this.colorSwatches = page.locator(".swatch-option.color");
        this.sizeSwatches = page.locator(".swatch-option.text");
        this.addToCartButton = page.locator("#product-addtocart-button");
        this.addToWishListButton = page.locator("a.action.towishlist");
        this.mainImage = page.locator("fotorama-stage");
        this.stockStatus = page.locator("div.stock span");
        this.outOfStockMessage = page.getByText("The requested qty is not available");
        this.miniCartIcon = page.locator("a.showcart");
        this.miniCartProductName = page.locator("#mini-cart strong.product-item-name a").first();
        this.miniCartProductPrice = page.locator(".minicart-items .price");
        this.miniCartViewCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View and Edit Cart"));
    }

    public String getProductName() {
        return productName.textContent().trim();
    }

    public String getProductPrice() {
        return productPrice.textContent().trim();
    }

    public List<Locator> getAvailableColorSwatches() {
        try {
            colorSwatches.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
            // If the wait succeeds, it's now safe to get all of them.
            return colorSwatches.all();
        } catch (Exception e) {
            // This happens if the product has NO color options.
            System.out.println("Product has no color swatches. Continuing.");
            return List.of(); // Return an empty list.
        }
    }

    public void selectRandomAvailableSize() {
        try {
            System.out.println("hi i should be here before 2 and after 1 ");
            // 1. Explicitly wait for the first size swatch to be visible.
            sizeSwatches.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
            // 2. Now that they are loaded, get only the ones that are NOT disabled.
            List<Locator> availableSizes = sizeSwatches.all();
            // 3. If there are any available sizes, click a random one.
            if (!availableSizes.isEmpty()) {
                System.out.println("Found " + availableSizes.size() + " available sizes. Selecting a random one.");
                availableSizes.get(2).click();
            } else {
                System.out.println("No available (enabled) sizes found for this product.");
            }
        } catch (Exception e) {
            // This happens if the product has NO size options at all.
            System.out.println("Product has no size swatches. Continuing.");
        }
    }

    public String getMainImageSrc() {
        return mainImage.getAttribute("src");
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public void addToWishList() {
        addToWishListButton.waitFor();
        addToWishListButton.click();
        System.out.println("trying to add to list ");
    }

    public Locator getStockStatus() {
        return stockStatus;
    }

    public Locator getOutOfStockMessage() {
        return outOfStockMessage;
    }

    public Locator getAddToCartButton() {
        return addToCartButton;
    }
}