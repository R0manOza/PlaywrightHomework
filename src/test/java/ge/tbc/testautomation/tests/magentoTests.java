package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoLoginAndRegistrationSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    public void colorChangeTest() {
        // Run the test for 3 random products . . . the test works as long as the picked product has more than 1 color or no color .
            homePageSteps.selectAndVerifyRandomHotSellerColorChange(3);

    }

    @Test(description = "Search for a product and verify it's added to the cart correctly")
    public void addToCartTest() {
        String[] productDetails = homePageSteps.searchAndAddToCart("shirt");
        addedProductName = productDetails[0];
        addedProductPrice = productDetails[1];

        homePageSteps.verifyItemInMiniCart(addedProductName, addedProductPrice);
    }

    @Test(description = "Delete the item added by the previous test from the cart", dependsOnMethods = "addToCartTest")
    public void deleteFromCartTest() {
        // 1. Start from a known state: the homepage.
        System.out.println("Navigating to homepage to begin delete test...");
        page.navigate("https://magento.softwaretestingboard.com/");


        System.out.println("Executing deleteFromCartTest, assuming item is in the cart...");
        cartSteps.navigateToCartAndDeleteFirstItem();


        cartSteps.verifyCartIsEmpty();
    }

    @Test(description = "Verify behavior for an out-of-stock item")
    public void outOfStockOfferTest() {
        productSteps.findOutOfStockItemAndVerifyStatus();
    }

    @Test(description = "Verify wishlist functionality for an unauthorized user")
    public void saveToFavoritesWhileUnauthorizedTest() {
        String productName = loginSteps.tryToAddItemToWishlistUnauthorized();
        loginSteps.verifyRedirectedToLoginPage();
        loginSteps.createNewAccountAndVerifyLogin();
        loginSteps.verifyItemWasAddedToWishlist(productName);
    }
}
