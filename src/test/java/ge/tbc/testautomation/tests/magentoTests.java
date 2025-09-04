package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoLoginAndRegistrationSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
@Epic("E-Commerce Functionality")
@Feature("General Product & Cart Interactions")
public class magentoTests extends BaseTest {
    // Steps classes are instantiated for each test
    private MagentoHomePageSteps homePageSteps;
    private MagentoCartSteps cartSteps;
    private MagentoProductPageSteps productSteps;
    private MagentoLoginAndRegistrationSteps loginSteps;

    // To share state between dependent tests
    private static String addedProductName;
    private static String addedProductPrice;

    @BeforeClass
    public void initializeSteps() {
        homePageSteps = new MagentoHomePageSteps(page);
        cartSteps = new MagentoCartSteps(page);
        productSteps = new MagentoProductPageSteps(page);
        loginSteps = new MagentoLoginAndRegistrationSteps(page);
    }

    @Test(description = "Verify product image changes when a new color is selected")
    @Story("User can visually confirm product color selection")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that when a user clicks on a different color swatch for a product on the homepage, the main product image updates accordingly.")
    public void colorChangeTest() {
        // Run the test for 3 random products . . . the test works as long as the picked product has more than 1 color or no color .
            homePageSteps.selectAndVerifyRandomHotSellerColorChange(3);

    }

    @Test(description = "Search for a product and verify it's added to the cart correctly")
    @Story("User adds and removes an item from the cart") // Same story as delete test to group them
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates the first part of the cart lifecycle: adding an item to the cart and verifying its details in the mini-cart.")
    public void addToCartTest() {
        String[] productDetails = homePageSteps.searchAndAddToCart("shirt");
        addedProductName = productDetails[0];
        addedProductPrice = productDetails[1];

        homePageSteps.verifyItemInMiniCart(addedProductName, addedProductPrice);
    }

    @Test(description = "Delete the item added by the previous test from the cart", dependsOnMethods = "addToCartTest")
    @Story("User adds and removes an item from the cart") // Same story as add test to group them
    @Severity(SeverityLevel.NORMAL)
    @Description("Dependent on the addToCartTest, this test validates the second part of the cart lifecycle: removing the previously added item and ensuring the cart becomes empty.")
    public void deleteFromCartTest() {
        // 1. Start from a known state: the homepage.
        System.out.println("Navigating to homepage to begin delete test...");
        page.navigate("https://magento.softwaretestingboard.com/");


        System.out.println("Executing deleteFromCartTest, assuming item is in the cart...");
        cartSteps.navigateToCartAndDeleteFirstItem();


        cartSteps.verifyCartIsEmpty();
    }
    @Test(description = "Verify behavior for an out-of-stock item")
    @Story("User is notified about out-of-stock items")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that when a user attempts to add an out-of-stock product variant to the cart, a clear message is displayed informing them that the requested quantity is not available.")
    public void outOfStockOfferTest() {
        productSteps.findOutOfStockItemAndVerifyStatus();
    }


    @Test(description = "Verify wishlist functionality for an unauthorized user")
    @Story("Guest user is prompted to register when using wishlist")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test validates a critical user acquisition funnel. It ensures that a guest user who tries to add an item to their wishlist is correctly redirected to the login/registration page, can create a new account, and that the item is successfully added to their wishlist after registration is complete.")
    public void saveToFavoritesWhileUnauthorizedTest() {
        String productName = loginSteps.tryToAddItemToWishlistUnauthorized();
        loginSteps.verifyRedirectedToLoginPage();
        loginSteps.createNewAccountAndVerifyLogin();
        loginSteps.verifyItemWasAddedToWishlist(productName);
    }
}
