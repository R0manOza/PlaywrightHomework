package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class magentomobileNavigationTest extends BaseTest{
    private MagentoProductPageSteps ProductSteps;
    private MagentoHomePageSteps HomeSteps;
    @BeforeClass
    public void initializeSteps() {
        HomeSteps = new MagentoHomePageSteps(page);
        ProductSteps = new MagentoProductPageSteps(page);
    }
    @Test(description = "Validates the website's responsive design by checking navigation link locations on desktop vs. mobile.")
    public void mobileNavigationTest() {
        HomeSteps.validateDesktopLayoutIsVisible()
                .resizeViewportToMobile()
                .validateMobileLayoutIsVisible()
                .openBurgerMenu().clickMobileAccount()
                .validateAccountLinksMovedToBurgerMenu().clickMobileMenu()
                .validateNavLinksMovedToBurgerMenu();
    }
}
