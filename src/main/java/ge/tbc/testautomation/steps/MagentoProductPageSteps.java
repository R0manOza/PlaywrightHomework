package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;

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
}
