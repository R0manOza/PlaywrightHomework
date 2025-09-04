package ge.tbc.testautomation.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
@Epic("E-Commerce Demo Site")
@Feature("User Account Interactions")
public class IsolatedUserTests {
    private static Playwright playwright;
    private static Browser browser;
    private Page page;
    private String favoriteProductName;
    private String email;
    private static final String password = "Coolpaswordthatis123!!";

    @BeforeClass
    public void setupClass() {
        playwright = Playwright.create();
        // Headless mode enabled for Step 3
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeMethod
    public void setupTest() {
        // New page for each test
        page = browser.newPage();

        // Create a new user for this test
        email = "testuser" + RandomStringUtils.randomAlphanumeric(8) + "@testing.com";

        page.navigate("https://practicesoftwaretesting.com/auth/register");

        page.locator("input[data-test='first-name']").fill("Test");
        page.locator("input[data-test='last-name']").fill("User");
        page.locator("input[data-test='dob']").fill("2000-01-01");
        page.locator("input[data-test='street']").fill("123 Test Street");
        page.locator("input[data-test='postal_code']").fill("12345");
        page.locator("input[data-test='city']").fill("Testville");
        page.locator("input[data-test='state']").fill("TS");
        page.locator("select[data-test='country']").selectOption("US");
        page.locator("input[data-test='phone']").fill(RandomStringUtils.randomNumeric(10));
        page.locator("input[data-test='email']").fill(email);
        page.locator("input[data-test='password']").fill(password);
        page.locator("button[data-test='register-submit']").click();

        // Ensure login page loaded
        assertThat(page.locator("h3:has-text('Login')")).isVisible();

        // Login immediately after registration
        page.locator("input[data-test='email']").fill(email);
        page.locator("input[data-test='password']").fill(password);
        page.locator("input[data-test='login-submit']").click();
        assertThat(page.locator("h1:has-text('My account')")).isVisible();

        // Go to homepage
        page.locator("a[data-test='nav-home']").click();
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
    @Test(description = "Verify that a user can add a product to their favorites.")
    @Story("A logged-in user should be able to add an item to their favorites list and see it there.")
    @Severity(SeverityLevel.CRITICAL)
    public void favouritesTest() {
        Locator productToFavorite = page.locator("a.card").first();
        String favoriteProductName = productToFavorite.locator("[data-test='product-name']").textContent().trim();
        productToFavorite.click();

        assertThat(page.locator("h1[data-test='product-name']")).hasText(favoriteProductName);
        page.locator("button[data-test='add-to-favorites']").click();

        page.locator("#toast-container .ngx-toastr").waitFor();
        page.locator("a[data-test='nav-menu']").click();
        page.locator("a[data-test='nav-my-favorites']").click();

        assertThat(page.locator("[data-test='product-name']:has-text('" + favoriteProductName + "')")).isVisible();
    }
    @Test(description = "Verify product filtering by category.")
    @Story("A user should be able to filter products by one or more categories and see the correct results.")
    @Severity(SeverityLevel.NORMAL)
    public void filterTest() {
        System.out.println("Starting filterTest...");
        assertThat(page.locator("a.card")).hasCount(9);
        int initialCount = page.locator("a.card").count();
        System.out.println("Initial total products on page: " + initialCount);
        assertThat(page.locator("a.card")).hasCount(initialCount);
        Locator handToolsCheckbox = page.locator("label:has-text('Screwdriver') input[type='checkbox']");
        Locator powerToolsCheckbox = page.locator("label:has-text('Sander') input[type='checkbox']");


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

        //
        assertThat(page.locator("a.card")).not().hasCount(powerToolsCount);
        int totalCount = page.locator("a.card").count();
        System.out.println("Found a total of " + totalCount + " products for both categories.");


        System.out.println("Asserting: " + totalCount + " should equal " + handToolsCount + " + " + powerToolsCount);
        assertEquals(totalCount, handToolsCount + powerToolsCount, "Combined filter count is incorrect.");

        System.out.println("Verified combined filter count is correct.");
    }

    @Test(description = "Verify that a user can remove a product from their favorites.")
    @Story("A logged-in user should be able to remove an item from their favorites list.")
    @Severity(SeverityLevel.NORMAL)
    public void removeFavouriteTest() {
        // First add a favourite
        Locator productToFavorite = page.locator("a.card").first();
        String favoriteProductName = productToFavorite.locator("[data-test='product-name']").textContent().trim();
        productToFavorite.click();
        page.locator("button[data-test='add-to-favorites']").click();
        page.locator("#toast-container .ngx-toastr").waitFor();

        // Now go to favourites page
        page.locator("a[data-test='nav-menu']").click();
        page.locator("a[data-test='nav-my-favorites']").click();
        assertThat(page.locator(".card-title:has-text('" + favoriteProductName + "')")).isVisible();

        // Remove it
        page.locator("button[data-test='delete']").click();
        assertThat(page.locator(".card-title:has-text('" + favoriteProductName + "')")).not().isVisible();
    }
    @Test(description = "Verify product tags are displayed correctly on the product page.")
    @Story("Navigating to a product page should display the correct product details and associated tags.")
    @Severity(SeverityLevel.MINOR)
    public void tagsTest() {
        page.locator("a[data-test='nav-categories']").click();
        page.locator("a[data-test='nav-hand-tools']").click();

        page.locator("a[data-test='product-01K3R5BHY0PECCB9K1CW1AGKBW']").click();
        assertThat(page.locator("h1[data-test='product-name']")).hasText(" Thor Hammer ");
        assertThat(page.locator("span.badge:has-text('Hammer')")).isVisible();
    }

}