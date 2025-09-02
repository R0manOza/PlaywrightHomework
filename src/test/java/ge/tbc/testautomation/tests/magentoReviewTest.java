package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoCartSteps;
import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoLoginAndRegistrationSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class magentoReviewTest extends BaseTest{
    private MagentoProductPageSteps ProductSteps;
    private MagentoHomePageSteps HomeSteps;
    @BeforeClass
    public void initializeSteps() {
        HomeSteps = new MagentoHomePageSteps(page);
        ProductSteps = new MagentoProductPageSteps(page);
    }
    @Test(description = "Validates that the review count on a product card matches the actual number of reviews on the product page.")
    public void reviewNumberTest() {

        HomeSteps.findFirstProductWithReviews().storeReviewCountFromCard();
        int expectedCount = HomeSteps.getExpectedReviewCount();
        HomeSteps.clickReviewLink();
        ProductSteps.switchToReviewsTab().validateReviewCount(expectedCount);
    }

}
