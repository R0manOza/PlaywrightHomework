package ge.tbc.testautomation.tests;


import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AdditionalScenariosTest extends BaseTest {

    private MagentoHomePageSteps homePageSteps;
    private MagentoCartSteps cartSteps;

    @BeforeClass
    public void initializeSteps() {
        homePageSteps = new MagentoHomePageSteps(page);
        cartSteps = new MagentoCartSteps(page);
    }

    /**
     * Automates Zephyr Scenario 1: Search, Add, and Verify Item in Cart
     */
    @Test(description = "E2E - Search for a product and verify successful addition to mini-cart.")
    public void searchAndVerifyItemInCartTest() {

        String[] productDetails = homePageSteps.searchAndAddToCart("shirt");
        String addedProductName = productDetails[0];
        String addedProductPrice = productDetails[1];


        homePageSteps.verifyItemInMiniCart(addedProductName, addedProductPrice);
    }

    /**
     * Automates Zephyr Scenario 2: Add and Then Remove Item from Cart
     */
    @Test(description = "E2E - Add an item to the cart and subsequently remove it.")
    public void addAndRemoveItemFromCartTest() {
        // Step 1-2: Add an item to the cart. We don't need its details for this test.
        homePageSteps.searchAndAddToCart("shirt");

        // Step 3-4: Go to the main cart and delete the item.
        cartSteps.navigateToCartAndDeleteFirstItem();

        // Step 5-6: Verify the cart is now empty.
        cartSteps.verifyCartIsEmpty();
    }
}