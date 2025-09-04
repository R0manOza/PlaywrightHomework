package ge.tbc.testautomation.tests;


import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Epic("E-Commerce Functionality")
@Feature("Shopping Cart Management")
public class AdditionalScenariosTest extends BaseTest {

    private MagentoHomePageSteps homePageSteps;
    private MagentoCartSteps cartSteps;

    @BeforeClass
    public void initializeSteps() {
        homePageSteps = new MagentoHomePageSteps(page);
        cartSteps = new MagentoCartSteps(page);
    }

    @Test(description = "E2E - Search for a product and verify successful addition to mini-cart.")
    @Story("A user should be able to find a product via search and confirm it was added to the cart.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test validates the core functionality of searching for a product, adding it to the cart, and verifying its presence in the mini-cart, which is a critical part of the user journey.")
    // i noticed my big mistake and forgot to add the scenario Number here
    public void searchAndVerifyItemInCartTest() {

        String[] productDetails = homePageSteps.searchAndAddToCart("shirt");
        String addedProductName = productDetails[0];
        String addedProductPrice = productDetails[1];


        homePageSteps.verifyItemInMiniCart(addedProductName, addedProductPrice);
    }

    @Test(description = "E2E - Add an item to the cart and subsequently remove it.")
    @Story("A user should be able to remove an item from their shopping cart.")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test validates the complete lifecycle of a cart item: adding it and then successfully removing it from the main cart page, ensuring the cart becomes empty.")
    public void addAndRemoveItemFromCartTest() {
        // Step 1-2: Add an item to the cart. We don't need its details for this test.
        homePageSteps.searchAndAddToCart("shirt");

        // Step 3-4: Go to the main cart and delete the item.
        cartSteps.navigateToCartAndDeleteFirstItem();

        // Step 5-6: Verify the cart is now empty.
        cartSteps.verifyCartIsEmpty();
    }
}