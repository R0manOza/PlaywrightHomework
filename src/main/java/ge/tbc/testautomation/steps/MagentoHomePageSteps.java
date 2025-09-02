package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;
import org.testng.Assert;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoHomePageSteps {
    private Locator productWithReviews;
    private final Page page;
    private final MagentoHomePage homePage;
    private final ProductPage productPage;
    private int expectedReviewCount;
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
    //-------------Playwright HW 3 functions -------------------
    public MagentoHomePageSteps findFirstProductWithReviews() {
        // Filter all product items to find the first one that has a review summary block.
        this.productWithReviews = homePage.productItem.filter(new Locator.FilterOptions().setHas(homePage.productReviewsSummary)).first();
        this.productWithReviews.waitFor();
        return this;
    }
    public MagentoHomePageSteps storeReviewCountFromCard() {
        String reviewCountText = this.productWithReviews.locator(homePage.productReviewLink).textContent().trim();
        String numberOnly = reviewCountText.replaceAll("[^0-9]", "");
        this.expectedReviewCount = Integer.parseInt(numberOnly);
        System.out.println("Found review count on card: " + this.expectedReviewCount);
        return this;
    }
    public MagentoHomePageSteps clickReviewLink() {
        this.productWithReviews.locator(homePage.productReviewLink).click();
        return this;
    }
    public int getExpectedReviewCount() {
        return this.expectedReviewCount;
    }
    //functions added for mobile test homework------------------------------------
    public MagentoHomePageSteps validateDesktopLayoutIsVisible() {

        Assert.assertTrue(homePage.headerSignInLink.isVisible(), "Sign In link should be visible in the header on desktop.");
        Assert.assertTrue(homePage.headerCreateAccountLink.isVisible(), "Create Account link should be visible in the header on desktop.");


        Assert.assertTrue(homePage.mainNavMenu.isVisible(), "Main navigation menu bar should be visible on desktop.");


        Assert.assertFalse(homePage.burgerMenuButton.isVisible(), "Burger menu button should NOT be visible on desktop.");
        System.out.println("Validated desktop layout is visible.");
        return this;
    }
    public MagentoHomePageSteps resizeViewportToMobile() {
        page.setViewportSize(390, 844); // Common iPhone 12/13/14 size
        System.out.println("Resized viewport to mobile dimensions.");
        return this;
    }
    public MagentoHomePageSteps clickMobileAccount() {
        homePage.mobileAccountMenu.click();
        System.out.println("Clicked the Account link in mobile menu.");
        return this;
    }
    public MagentoHomePageSteps clickMobileMenu() {
        homePage.mobileMenu.click();
        System.out.println("Clicked the Menu link in mobile menu.");
        return this;
    }

    public MagentoHomePageSteps validateMobileLayoutIsVisible() {

        homePage.burgerMenuButton.waitFor();


        Assert.assertTrue(homePage.burgerMenuButton.isVisible(), "Burger menu button should be visible on mobile.");


        Assert.assertFalse(homePage.headerSignInLink.isVisible(), "Sign In link should NOT be visible in the header on mobile.");
        Assert.assertFalse(homePage.headerCreateAccountLink.isVisible(), "Create Account link should NOT be visible in the header on mobile.");
        System.out.println("Validated mobile layout is visible.");
        return this;
    }
    public MagentoHomePageSteps openBurgerMenu() {
        homePage.burgerMenuButton.click();
        homePage.mobileMenuPanel.waitFor();
        System.out.println("Clicked the burger menu.");
        return this;
    }
    public MagentoHomePageSteps validateAccountLinksMovedToBurgerMenu() {




        homePage.signInLinkInMenu.waitFor();

        Assert.assertTrue(homePage.signInLinkInMenu.isVisible(), "Sign In link should be visible in the burger menu.");
        System.out.println("Validated account links are inside the burger menu.");
        return this;
    }
    public MagentoHomePageSteps validateNavLinksMovedToBurgerMenu() {
        // Check for a few key links to confirm they are in the right place.
        Locator whatsNewLink = homePage.mobileMenuNavLinks.locator("a:has-text(\"WHAT'S NEW\")");
        Locator womenLink = homePage.mobileMenuNavLinks.locator("a:has-text('WOMEN')").first();
        Locator saleLink = homePage.mobileMenuNavLinks.locator("a:has-text('SALE')");

        Assert.assertTrue(whatsNewLink.isVisible(), "'What's New' link should be visible in the burger menu.");
        Assert.assertTrue(womenLink.isVisible(), "'Women' link should be visible in the burger menu.");
        Assert.assertTrue(saleLink.isVisible(), "'Sale' link should be visible in the burger menu.");
        System.out.println("Validated main navigation links are inside the burger menu.");
        return this;
    }


}
