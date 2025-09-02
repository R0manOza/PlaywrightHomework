package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
public class MagentoLoginAndRegistrationSteps {
    private final Page page;
    private final ProductPage productPage;
    private final MagentoCreateAccountPage createAccountPage;
    private final MagentoWishListPage wishListPage;
    private final MagentoLoginPage loginPage;
    private final MagentoHomePage homePage;
    public MagentoLoginAndRegistrationSteps(Page page) {
        this.page = page;
        this.productPage = new ProductPage(page);
        this.createAccountPage = new MagentoCreateAccountPage(page);
        this.wishListPage = new MagentoWishListPage(page);
        this.loginPage = new MagentoLoginPage(page);
        this.homePage = new MagentoHomePage(page);
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
    public MagentoLoginAndRegistrationSteps login(String email, String password) {
        page.navigate("https://magento.softwaretestingboard.com/customer/account/login/");
        loginPage.emailInput.fill(email);
        loginPage.passwordInput.fill(password);
        loginPage.signInButton.click();
        System.out.println("Step: Logged in with user: " + email);
        return this;
    }

    public MagentoLoginAndRegistrationSteps registerNewUser(String firstName, String lastName, String email, String password) {
        page.navigate("https://magento.softwaretestingboard.com/customer/account/create/");
        createAccountPage.registerUser(firstName, lastName, email, password);
        System.out.println("Step: Created a new account for user: " + email);


        assertThat(wishListPage.getWelcomeMessage()).containsText("Welcome, " + firstName + " " + lastName + "!");
        System.out.println("Verification: Welcome message is correct, user is logged in.");

        return this;
    }
    public MagentoLoginAndRegistrationSteps logout() {
        homePage.customerMenuDropdown.waitFor();//i have to click this manually because none of the 100 locators worked for me. WHY???
        homePage.customerMenuDropdown.click();
        homePage.signOutLink.click();
        System.out.println("Step: User logged out.");
        return this;
    }
    }

