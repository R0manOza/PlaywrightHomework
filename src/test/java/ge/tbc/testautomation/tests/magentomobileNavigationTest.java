package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.steps.MagentoHomePageSteps;
import ge.tbc.testautomation.steps.MagentoProductPageSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
//magento serverrs are still down ...
@Epic("E-Commerce Functionality")
@Feature("Responsive Design")
public class magentomobileNavigationTest extends BaseTest{
    private MagentoProductPageSteps ProductSteps;
    private MagentoHomePageSteps HomeSteps;
    @BeforeClass
    public void initializeSteps() {
        HomeSteps = new MagentoHomePageSteps(page);
        ProductSteps = new MagentoProductPageSteps(page);
    }
    @Test(description = "Validates responsive design by checking navigation links on desktop vs. mobile.")
    @Story("As a mobile user, I want to access all navigation links through a burger menu for a clean UI.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test first verifies the standard desktop navigation, then resizes the screen to a mobile viewport, and finally asserts that the main and account navigation links have correctly moved inside the burger menu.")
    public void mobileNavigationTest() {
        HomeSteps.validateDesktopLayoutIsVisible()
                .resizeViewportToMobile()
                .validateMobileLayoutIsVisible()
                .openBurgerMenu().clickMobileAccount()
                .validateAccountLinksMovedToBurgerMenu().clickMobileMenu()
                .validateNavLinksMovedToBurgerMenu();
    }
}
