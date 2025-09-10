package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;
import io.qameta.allure.Step;
import org.testng.Assert;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoProductPageSteps {
    private final Page page;
    private final MagentoHomePage homePage;
    private final ProductPage productPage;

    public MagentoProductPageSteps(Page page) {
        this.page = page;
        this.homePage = new MagentoHomePage(page);
        this.productPage = new ProductPage(page);
    }
    @Step("Find an out-of-stock item and verify its status message")
    public void findOutOfStockItemAndVerifyStatus() {
        homePage.searchForProduct("Radiant Tee");
        page.locator(".product-item-link:has-text('Radiant Tee')").first().click();
        System.out.println("Step: Navigated to Radiant Tee product page.");

        // Select a size known to be out of stock
        page.locator(".swatch-option.text[option-label='XS']").click();
        System.out.println("Step: Selected XS size.");
        productPage.getAvailableColorSwatches().get(0).click();
        productPage.addToCart();
        assertThat(productPage.getOutOfStockMessage()).isVisible();
        System.out.println("Verification: 'Out of stock' message is visible.");// kinda
        // i looked around the page but i couldn't find any itmes that said out of stock , this quantity not available was the closest thing i found
    }
    @Step("Switch to the 'Reviews' tab on the product page")
    public MagentoProductPageSteps switchToReviewsTab() {
        productPage.reviewsTab.click();
        return this;
    }
    @Step("Validate that the review count on the page matches the expected count: {expectedCount}")
    public MagentoProductPageSteps validateReviewCount(int expectedCount) {

        productPage.reviewItem.first().waitFor();
        int actualCount = productPage.reviewItem.count();
        //System.out.println("Found " + actualCount + " reviews on product page.");

        Assert.assertEquals(actualCount, expectedCount, "The actual review count should match the expected count.");
        return this;
    }
}
