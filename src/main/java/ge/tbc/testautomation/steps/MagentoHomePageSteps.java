package ge.tbc.testautomation.steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;
import io.qameta.allure.Step; // Added import
import org.testng.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    @Step("Select and verify color change for {numberOfProductsToTest} random hot seller products")
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
                Locator firstColor = colorSwatches.nth(0);
                Locator secondColor = colorSwatches.nth(1);

                assertThat(secondColor).not().hasClass(Pattern.compile(".*selected.*"));
                secondColor.click();
                assertThat(secondColor).hasClass(Pattern.compile(".*selected.*"));
                assertThat(firstColor).not().hasClass(Pattern.compile(".*selected.*"));

                productsTested++;
            }
        }
    }

    @Step("Search for '{searchTerm}', select the first result, and add it to the cart")
    public String[] searchAndAddToCart(String searchTerm) {
        homePage.searchForProduct(searchTerm);
        page.locator(".product-item").first().waitFor();
        page.locator(".product-item").first().click();

        String productName = productPage.getProductName();
        String productPrice = productPage.getProductPrice();

        productPage.selectRandomAvailableSize();
        productPage.getAvailableColorSwatches().get(0).click();
        productPage.addToCart();

        page.locator(".message-success").first().waitFor();
        assertThat(page.locator(".message-success")).isVisible();

        return new String[]{productName, productPrice};
    }

    @Step("Verify item in mini-cart. Expected Name: '{expectedName}', Expected Price: '{expectedPrice}'")
    public void verifyItemInMiniCart(String expectedName, String expectedPrice) {
        String actualName = homePage.getProductNameFromMiniCart();
        String actualPrice = homePage.getProductPriceFromMiniCart();

        if (!Objects.equals(actualName, expectedName)) {
            throw new AssertionError("Product name in mini-cart is incorrect. Expected: " + expectedName + ", but was: " + actualName);
        }
        if (!Objects.equals(actualPrice, expectedPrice)) {
            throw new AssertionError("Product price in mini-cart is incorrect. Expected: " + expectedPrice + ", but was: " + actualPrice);
        }
    }

    @Step("Find the first product on the page that has reviews")
    public MagentoHomePageSteps findFirstProductWithReviews() {
        this.productWithReviews = homePage.productItem.filter(new Locator.FilterOptions().setHas(homePage.productReviewsSummary)).first();
        this.productWithReviews.waitFor();
        return this;
    }

    @Step("Read and store the review count from the product card")
    public MagentoHomePageSteps storeReviewCountFromCard() {
        String reviewCountText = this.productWithReviews.locator(homePage.productReviewLink).textContent().trim();
        String numberOnly = reviewCountText.replaceAll("[^0-9]", "");
        this.expectedReviewCount = Integer.parseInt(numberOnly);
        return this;
    }

    @Step("Click the review link on the product card")
    public MagentoHomePageSteps clickReviewLink() {
        this.productWithReviews.locator(homePage.productReviewLink).click();
        return this;
    }

    //didn't give getter @step anotation because it's a helper method not a full step
    public int getExpectedReviewCount() {
        return this.expectedReviewCount;
    }

    @Step("Validate that the desktop navigation layout is visible")
    public MagentoHomePageSteps validateDesktopLayoutIsVisible() {
        Assert.assertTrue(homePage.headerSignInLink.isVisible(), "Sign In link should be visible in the header on desktop.");
        Assert.assertTrue(homePage.headerCreateAccountLink.isVisible(), "Create Account link should be visible in the header on desktop.");
        Assert.assertTrue(homePage.mainNavMenu.isVisible(), "Main navigation menu bar should be visible on desktop.");
        Assert.assertFalse(homePage.burgerMenuButton.isVisible(), "Burger menu button should NOT be visible on desktop.");
        return this;
    }

    @Step("Resize browser viewport to mobile dimensions (390x844)")
    public MagentoHomePageSteps resizeViewportToMobile() {
        page.setViewportSize(390, 844);
        return this;
    }

    @Step("In mobile view, click the 'Account' menu link")
    public MagentoHomePageSteps clickMobileAccount() {
        homePage.mobileAccountMenu.click();
        return this;
    }

    @Step("In mobile view, click the 'Menu' link")
    public MagentoHomePageSteps clickMobileMenu() {
        homePage.mobileMenu.click();
        return this;
    }

    @Step("Validate that the mobile navigation layout (burger menu) is visible")
    public MagentoHomePageSteps validateMobileLayoutIsVisible() {
        homePage.burgerMenuButton.waitFor();
        Assert.assertTrue(homePage.burgerMenuButton.isVisible(), "Burger menu button should be visible on mobile.");
        Assert.assertFalse(homePage.headerSignInLink.isVisible(), "Sign In link should NOT be visible in the header on mobile.");
        Assert.assertFalse(homePage.headerCreateAccountLink.isVisible(), "Create Account link should NOT be visible in the header on mobile.");
        return this;
    }

    @Step("Open the mobile burger menu")
    public MagentoHomePageSteps openBurgerMenu() {
        homePage.burgerMenuButton.click();
        homePage.mobileMenuPanel.waitFor();
        return this;
    }

    @Step("Validate that 'Sign In' link has moved into the burger menu")
    public MagentoHomePageSteps validateAccountLinksMovedToBurgerMenu() {
        homePage.signInLinkInMenu.waitFor();
        Assert.assertTrue(homePage.signInLinkInMenu.isVisible(), "Sign In link should be visible in the burger menu.");
        return this;
    }

    @Step("Validate that main navigation links have moved into the burger menu")
    public MagentoHomePageSteps validateNavLinksMovedToBurgerMenu() {
        Locator whatsNewLink = homePage.mobileMenuNavLinks.locator("a:has-text(\"WHAT'S NEW\")");
        Locator womenLink = homePage.mobileMenuNavLinks.locator("a:has-text('WOMEN')").first();
        Locator saleLink = homePage.mobileMenuNavLinks.locator("a:has-text('SALE')");

        Assert.assertTrue(whatsNewLink.isVisible(), "'What's New' link should be visible in the burger menu.");
        Assert.assertTrue(womenLink.isVisible(), "'Women' link should be visible in the burger menu.");
        Assert.assertTrue(saleLink.isVisible(), "'Sale' link should be visible in the burger menu.");
        return this;
    }
}