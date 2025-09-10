package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoLoginAndRegistrationSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@Epic("E-Commerce Functionality")
@Feature("Product Details & Reviews")
public class magentoReviewTest extends BaseTest{
    private MagentoProductPageSteps ProductSteps;
    private MagentoHomePageSteps HomeSteps;
    @BeforeClass
    public void initializeSteps() {
        HomeSteps = new MagentoHomePageSteps(page);
        ProductSteps = new MagentoProductPageSteps(page);
    }
    @Test(description = "Validates that the review count on a product card matches the actual number of reviews.")
    @Story("As a user, I want to trust the review count displayed on product cards, so it should match the actual reviews on the product page.")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test finds a product with reviews on the main page, reads the displayed review count, navigates to the product's detail page, and asserts that the actual number of reviews shown matches the count from the main page.")
    public void reviewNumberTest() {

        HomeSteps.findFirstProductWithReviews().storeReviewCountFromCard();
        int expectedCount = HomeSteps.getExpectedReviewCount();
        HomeSteps.clickReviewLink();
        ProductSteps.switchToReviewsTab().validateReviewCount(expectedCount);
    }

}
