package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoCreateAccountPage;
import ge.tbc.testautomation.pages.MagentoWishListPage;
import ge.tbc.testautomation.pages.ProductPage;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoLoginAndRegistrationSteps {
    private final Page page;
    private final ProductPage productPage;
    private final MagentoCreateAccountPage createAccountPage;
    private final MagentoWishListPage wishListPage;

    public MagentoLoginAndRegistrationSteps(Page page) {
        this.page = page;
        this.productPage = new ProductPage(page);
        this.createAccountPage = new MagentoCreateAccountPage(page);
        this.wishListPage = new MagentoWishListPage(page);
    }

    public String tryToAddItemToWishlistUnauthorized() {
        page.navigate("https://magento.softwaretestingboard.com/radiant-tee.html");
        String productName = productPage.getProductName();

        productPage.addToWishList();
        System.out.println("Step: Attempted to add '" + productName + "' to wishlist while not logged in.");
        return productName;
    }

    public void verifyRedirectedToLoginPage() {
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*customer/account/login.*"));
        System.out.println("Verification: Redirected to login page as expected.");

    }

    public String[] createNewAccountAndVerifyLogin() {
        page.locator("a.action.create.primary").click();

        String firstName = "Test" + RandomStringUtils.randomNumeric(3);
        String lastName = "User" + RandomStringUtils.randomNumeric(3);
        String email = "test" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        String password = "StrongPassword123$";

        createAccountPage.registerUser(firstName, lastName, email, password);
        System.out.println("Step: Created a new account for user: " + firstName + " " + lastName);

        assertThat(wishListPage.getWelcomeMessage()).containsText("Welcome, " + firstName + " " + lastName + "!");
        System.out.println("Verification: Welcome message is correct.");

        return new String[]{firstName, lastName};
    }

    public void verifyItemWasAddedToWishlist(String productName) {
        assertThat(page).hasURL(Pattern.compile(".*wishlist.*"));
        assertThat(wishListPage.getSuccessMessage()).isVisible();

        wishListPage.verifyProductInWishList(productName);
        System.out.println("Verification: Product was successfully added to wishlist after registration.");
    }
    }

