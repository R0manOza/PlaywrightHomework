package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoHomePageSteps {
    private final Page page;
    private final MagentoHomePage homePage;
    private final ProductPage productPage;
    public MagentoHomePageSteps(Page page) {
        this.page = page;
        this.homePage = new MagentoHomePage(page);
        this.productPage = new ProductPage(page);
    }
    public void selectAndVerifyRandomHotSellerColorChange(int numberOfProductsToTest) {
        List<Locator> allSellers = homePage.getHotSellers();
        Collections.shuffle(allSellers);

        int productsTested = 0;

        for (Locator seller : allSellers) {
            if (productsTested >= numberOfProductsToTest) {
                break;
            }

            Locator colorSwatches = homePage.getColorSwatchesForProduct(seller);

            if (colorSwatches.count() > 1) {
                String productName = seller.locator(".product-item-name").textContent();
                System.out.println("\n--- Testing color change for product: " + productName.trim() + " ---");

                // Identify the first and second color swatches
                Locator firstColor = colorSwatches.nth(0);
                Locator secondColor = colorSwatches.nth(1);

                // --- **THE NEW VERIFICATION LOGIC** ---

                // 1. Assert that initially, the first color is selected OR no color is selected.
                // We check if the second one is NOT selected.
                assertThat(secondColor).not().hasClass(Pattern.compile(".*selected.*"));
                String initialSelectedColorLabel = firstColor.getAttribute("aria-label");
                System.out.println("Initial selected color appears to be: " + initialSelectedColorLabel);

                // 2. Click the second color swatch.
                secondColor.click();

                // 3. **EXPLICIT WAIT & VERIFICATION:** Wait until the second swatch has the 'selected' class.
                // This is the most reliable way to confirm the action was successful.
                assertThat(secondColor).hasClass(Pattern.compile(".*selected.*"));
                System.out.println("Verification successful: '" + secondColor.getAttribute("aria-label") + "' is now selected.");

                // 4. (Optional but good) Verify the first swatch is now NOT selected.
                assertThat(firstColor).not().hasClass(Pattern.compile(".*selected.*"));

                productsTested++;
            }
        }

        if (productsTested < numberOfProductsToTest) {
            System.out.println("Warning: Could only find and test " + productsTested + " products with multiple colors on the homepage.");
        }
    }

    public String[] searchAndAddToCart(String searchTerm) {
        homePage.searchForProduct(searchTerm);
        page.locator(".product-item").first().waitFor();
        page.locator(".product-item").first().click();

        String productName = productPage.getProductName();
        String productPrice = productPage.getProductPrice();
        System.out.println("hi im here 1");
        productPage.selectRandomAvailableSize();


        productPage.getAvailableColorSwatches().get(0).click();
        System.out.println("hi im here 2");
        productPage.addToCart();

        page.locator(".message-success").first().waitFor();
        assertThat(page.locator(".message-success")).isVisible();
        System.out.println("Step: Added '" + productName + "' to cart.");

        return new String[]{productName, productPrice};
    }

    public void verifyItemInMiniCart(String expectedName, String expectedPrice) {
        String actualName = homePage.getProductNameFromMiniCart();
        String actualPrice = homePage.getProductPriceFromMiniCart();

        // --- **THE FIX IS HERE: Using plain Java for assertion** ---
        if (!Objects.equals(actualName, expectedName)) {
            throw new AssertionError("Product name in mini-cart is incorrect. Expected: " + expectedName + ", but was: " + actualName);
        }
        if (!Objects.equals(actualPrice, expectedPrice)) {
            throw new AssertionError("Product price in mini-cart is incorrect. Expected: " + expectedPrice + ", but was: " + actualPrice);
        }

        System.out.println("Verification: Product details in mini-cart are correct.");
    }
}
