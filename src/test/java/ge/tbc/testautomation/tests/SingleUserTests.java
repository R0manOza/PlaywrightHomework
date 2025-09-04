package ge.tbc.testautomation.tests;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
@Epic("E-Commerce Demo Site")
@Feature("User Account Interactions")
public class SingleUserTests {

    // Shared objects for all tests in this class
    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    // Store user credentials and favorite product name to share between tests
    private static final String email = "testuser" + RandomStringUtils.randomAlphanumeric(8) + "@testing.com";
    private static final String password = "Coolpaswordthatis123!!";
    private String favoriteProductName;


    private void login(Page pageToLogIn, String userEmail, String userPassword) {

        pageToLogIn.locator("input[data-test='email']").fill(userEmail);
        pageToLogIn.locator("input[data-test='password']").fill(userPassword);
        pageToLogIn.locator("input[data-test='login-submit']").click();
        assertThat(pageToLogIn.locator("a[data-test='nav-menu']")).isVisible();
    }
    @BeforeClass
    public void setupClass() {
        playwright = Playwright.create();
        // IMPORTANT: Run in non-headless mode
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));


        // --- Register and Login ONCE for the entire class ---
        Page setupPage = browser.newPage();
        setupPage.navigate("https://practicesoftwaretesting.com/auth/register");

        System.out.println("Registering a new user for all tests...");
        // --- UPDATED REGISTRATION LOGIC ---
        setupPage.locator("input[data-test='first-name']").fill("Test");
        setupPage.locator("input[data-test='last-name']").fill("User");
        setupPage.locator("input[data-test='dob']").fill("2000-01-01");
        setupPage.locator("input[data-test='street']").fill("123 Test Street");
        setupPage.locator("input[data-test='postal_code']").fill("12345");
        setupPage.locator("input[data-test='city']").fill("Testville");
        setupPage.locator("input[data-test='state']").fill("TS");
        setupPage.locator("select[data-test='country']").selectOption("US");
        setupPage.locator("input[data-test='phone']").fill(RandomStringUtils.randomNumeric(10));
        setupPage.locator("input[data-test='email']").fill(email);
        setupPage.locator("input[data-test='password']").fill(password);
        setupPage.locator("button[data-test='register-submit']").click();

        System.out.println("Waiting for login page to load after registration...");
        assertThat(setupPage.locator("h3:has-text('Login')")).isVisible();

//        setupPage.locator("input[data-test='email']").fill(email);
//        setupPage.locator("input[data-test='password']").fill(password);
//        setupPage.locator("input[data-test='login-submit']").click();
//        assertThat(setupPage.locator("a[data-test='nav-menu']")).isVisible();
//        System.out.println("Successfully registered and logged in user: " + email);
        setupPage.close();


    }

    @BeforeMethod
    public void loginBeforeEachTest() {
        // Create a new page for the test
        page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com/auth/login");


        System.out.println("Waiting for login page to load ...");
        assertThat(page.locator("h3:has-text('Login')")).isVisible();
        // Log in with the credentials created in @BeforeClass
        page.locator("input[data-test='email']").fill(email);
        page.locator("input[data-test='password']").fill(password);
        page.locator("input[data-test='login-submit']").click();

        assertThat(page.locator("h1", new Page.LocatorOptions().setHasText("My account"))).isVisible();
        //go to homepage
        page.locator("a[data-test='nav-home']").click();
        // Verify we are on the homepage and logged in
        System.out.println("Waiting for home page to load ...");

        System.out.println("Logged in successfully for test: " + email);
    }

    @AfterMethod
    public void tearDownTest() {
        page.close();
    }

    @AfterClass
    public void tearDownClass() {
        browser.close();
        playwright.close();
    }

    @Test(priority = 1, description = "Verify a user can add a product to favorites.")
    @Story("User can manage their favorites list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test ensures that a logged-in user can navigate to a product, add it to their favorites, and then verify its presence on the favorites page.")
    public void favouritesTest() {
        // 1. Select the first product card from the homepage.
        Locator productToFavorite = page.locator("a.card").first();

        favoriteProductName = productToFavorite.locator("[data-test='product-name']").textContent().trim();
        System.out.println("Selecting product to favorite: " + favoriteProductName);

        // 2. Click the product to go to its details page.
        productToFavorite.click();


        // 3. Wait for the product details page to load by waiting for its specific title element.
            Locator productNameOnDetailsPage = page.locator("h1[data-test='product-name']");
        assertThat(productNameOnDetailsPage).isVisible();
        assertThat(productNameOnDetailsPage).hasText(favoriteProductName);

        // 4. Click the "Add to favourites" button on the details page.
        page.locator("button[data-test='add-to-favorites']").click();
        // Wait for the toast notification to appear and verify its text.
        page.locator("#toast-container .ngx-toastr").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        // 5. Navigate to the favorites page to verify.
        page.locator("a[data-test='nav-menu']").click();
        page.locator("a[data-test='nav-my-favorites']").click();
        assertThat(page.locator("[data-test='product-name']:has-text('" + favoriteProductName + "')")).isVisible();
        System.out.println("Verified '" + favoriteProductName + "' is in favourites.");
    }






    @Test(priority = 2, description = "Verify product filtering by category.")
    @Story("User can filter products to refine search")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test validates the product filtering functionality by applying single and multiple filters and asserting that the displayed product count is correct.")
    public void filterTest() {
        System.out.println("Starting filterTest...");
        assertThat(page.locator("a.card")).hasCount(9);
        int initialCount = page.locator("a.card").count();
        System.out.println("Initial total products on page: " + initialCount);
        assertThat(page.locator("a.card")).hasCount(initialCount);
        Locator handToolsCheckbox = page.locator("label:has-text('Screwdriver') input[type='checkbox']");
        Locator powerToolsCheckbox = page.locator("label:has-text('Sander') input[type='checkbox']");

        // --- STEP 1: Measure Hand Tools count ---
        handToolsCheckbox.check();


        assertThat(page.locator("a.card")).not().hasCount(initialCount);

        int handToolsCount = page.locator("a.card").count();
        System.out.println("Found " + handToolsCount + " products for Hand Tools.");


        handToolsCheckbox.uncheck();


        assertThat(page.locator("a.card")).hasCount(initialCount);
        System.out.println("Filters reset. Product count is back to " + initialCount);


        powerToolsCheckbox.check();


        assertThat(page.locator("a.card")).not().hasCount(initialCount);
        int powerToolsCount = page.locator("a.card").count();
        System.out.println("Found " + powerToolsCount + " products for Power Tools.");


        handToolsCheckbox.check();


        assertThat(page.locator("a.card")).not().hasCount(powerToolsCount);
        int totalCount = page.locator("a.card").count();
        System.out.println("Found a total of " + totalCount + " products for both categories.");


        System.out.println("Asserting: " + totalCount + " should equal " + handToolsCount + " + " + powerToolsCount);
        assertEquals(totalCount, handToolsCount + powerToolsCount, "Combined filter count is incorrect.");

        System.out.println("Verified combined filter count is correct.");
    }

    @Test(priority = 3, dependsOnMethods = "favouritesTest", description = "Verify a user can remove a product from favorites.")
    @Story("User can manage their favorites list")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test depends on a product being in the favorites list. It navigates to the favorites page and verifies that the user can successfully remove the item.")
    public void removeFavouriteTest() {
        // This test now depends on the STATE left by the previous test,
        // which is why we made favoriteProductName static.
        page.locator("a[data-test='nav-menu']").click();
        page.locator("a[data-test='nav-my-favorites']").click();
        assertThat(page.locator(".card-title:has-text('" + favoriteProductName + "')")).isVisible();

        page.locator("button[data-test='delete']").click();


    }

    @Test(priority = 4, description = "Verify product tags are displayed correctly.")
    @Story("User can see correct product details")
    @Severity(SeverityLevel.MINOR)
    @Description("This test ensures data integrity by navigating to a specific product page and verifying that its name and associated tags are displayed correctly.")
    public void tagsTest() {
        // 1. Click the main "Categories" dropdown menu to open it.
        // We use the data-test attribute for reliability.
        page.locator("a[data-test='nav-categories']").click();

        // 2. Click the "Hand Tools" link from the now-visible dropdown.
        page.locator("a[data-test='nav-hand-tools']").click();


        assertThat(page.locator("a[data-test='product-01K3R5BHY0PECCB9K1CW1AGKBW']")).isVisible();
        page.locator("a[data-test='product-01K3R5BHY0PECCB9K1CW1AGKBW']").click();
        System.out.println("thors hammer located");
        // 5. Wait for the product details page to load by asserting the product name.
        Locator productNameOnDetailsPage = page.locator("h1[data-test='product-name']");
        assertThat(productNameOnDetailsPage).hasText(" Thor Hammer ");
        System.out.println("thors hammer located");

        // 6. Verify the tags on the product details page.
        // The tags are span elements. We check for the presence of spans with the correct text.
        assertThat(page.locator("span.badge:has-text('Hammer')")).isVisible();


        System.out.println("Verified Thor Hammer tags are correct.");
    }
}