package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoCheckoutCartPage;
import ge.tbc.testautomation.pages.MagentoHomePage;
import ge.tbc.testautomation.pages.ProductPage;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoCartSteps {
    private final MagentoHomePage homePage;
    private final MagentoCheckoutCartPage checkoutCartPage;
    private final ProductPage ProductPage;

    public MagentoCartSteps(Page page) {
        this.homePage = new MagentoHomePage(page);
        this.checkoutCartPage = new MagentoCheckoutCartPage(page);
        this.ProductPage = new ProductPage(page);
    }

    public void navigateToCartAndDeleteFirstItem() {
        homePage.goToCheckoutCart();
        System.out.println("Step: Navigated to the main shopping cart page.");

        checkoutCartPage.deleteFirstItem();
        System.out.println("Step: Clicked the delete button for the first item.");
    }

    public void verifyCartIsEmpty() {
        assertThat(checkoutCartPage.getEmptyCartMessage()).isVisible();
        assertThat(checkoutCartPage.getEmptyCartMessage()).containsText("You have no items in your shopping cart.");
        System.out.println("Verification: Cart is now empty.");
    }
}
