package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.pages.ProductPage;
import ge.tbc.testautomation.steps.MagentoCheckoutSteps;
import ge.tbc.testautomation.steps.MagentoLoginAndRegistrationSteps;
import ge.tbc.testautomation.steps.MagentoWishlistSteps;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@Epic("E-Commerce Functionality")
@Feature("Purchase Flow")
public class magentoPurchaseTest extends BaseTest {

    private MagentoLoginAndRegistrationSteps loginSteps;
    private MagentoWishlistSteps wishlistSteps;
    private MagentoCheckoutSteps checkoutSteps;
    private ProductPage productPage;

    @BeforeClass
    public void initializeSteps() {
        loginSteps = new MagentoLoginAndRegistrationSteps(page);
        wishlistSteps = new MagentoWishlistSteps(page);
        checkoutSteps = new MagentoCheckoutSteps(page);
        productPage = new ProductPage(page);
    }

    @Test(description = "E2E - Full purchase cycle for a new user starting from the wishlist.")
    @Story("A new user should be able to register, add an item to their wishlist, log out, log back in, and purchase that item.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This is a critical path test that validates user registration, wishlist persistence after logout/login, the entire multi-step checkout process including address validation and payment steps, and final order confirmation.")    public void purchaseItem() {
        // --- Test Data ---
        String firstName = "Test" + RandomStringUtils.randomNumeric(3);
        String lastName = "User" + RandomStringUtils.randomNumeric(3);
        String email = "testuser" + RandomStringUtils.randomAlphanumeric(5) + "@example.com";
        String password = "StrongPassword123$";
        String wishedProductName = "Radiant Tee";

        // --- The Test Story ---

        // --- Part 1: Create the User and Set the Initial State (Item in Wishlist) ---
        loginSteps.registerNewUser(firstName, lastName, email, password);

        page.navigate("https://magento.softwaretestingboard.com/radiant-tee.html");
        productPage.selectRandomAvailableSize();


        productPage.getAvailableColorSwatches().get(0).click();
        productPage.addToWishList();

        loginSteps.logout();

        // --- Part 2: Main Test - Log In and Verify Wishlist Persists ---
        loginSteps.login(email, password);

        wishlistSteps.navigateToWishlist()
                .verifyItemIsPresentInWishlist(wishedProductName);

        // --- Part 3: Proceed with Purchase ---
        wishlistSteps.addItemFromWishlistToCart(wishedProductName);

        checkoutSteps.proceedToCheckout();

        // --- Part 4: Fill Shipping and Validate Payment Page ---
        String street = "123 Test Street";
        String city = "Testville";
        String postcode = "12345";
        String phone = "1234567890";

        checkoutSteps.fillShippingDetailsIfEmpty(street, city, "1", postcode, "US", phone) // '1' is value for Alabama
                .clickNextToReviewPayments();

        checkoutSteps.validateShippingCostIsAddedToTotal()
                .validateAccountInfoIsCorrect(firstName, lastName, street, city, postcode, phone)
                .applyInvalidDiscountCodeAndVerifyError();

        // --- Part 5: Place Order and Verify Success ---
        checkoutSteps.placeOrder()
                .verifyOrderSuccess();
    }
}