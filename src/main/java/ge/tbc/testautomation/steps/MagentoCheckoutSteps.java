package ge.tbc.testautomation.steps;

import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.MagentoCheckoutPage;
import ge.tbc.testautomation.pages.MagentoSuccessPage;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MagentoCheckoutSteps {
    private final Page page;
    private final MagentoCheckoutPage checkoutPage;
    private final MagentoSuccessPage successPage;

    public MagentoCheckoutSteps(Page page) {
        this.page = page;
        this.checkoutPage = new MagentoCheckoutPage(page);
        this.successPage = new MagentoSuccessPage(page);
    }

    public MagentoCheckoutSteps proceedToCheckout() {
        page.navigate("https://magento.softwaretestingboard.com/checkout/");
        checkoutPage.streetAddressInput.waitFor();
        System.out.println("Step: Navigated to checkout page.");
        return this;
    }

    public MagentoCheckoutSteps fillShippingDetailsIfEmpty(String street, String city, String region, String postcode, String country, String phone) {
        if (checkoutPage.streetAddressInput.inputValue().isEmpty()) {
            System.out.println("Step: Shipping form is empty, filling details...");
            checkoutPage.streetAddressInput.fill(street);
            checkoutPage.cityInput.fill(city);
            checkoutPage.regionDropdown.selectOption(region);
            checkoutPage.postCodeInput.fill(postcode);
            checkoutPage.countryDropdown.selectOption(country);
            checkoutPage.telephoneInput.fill(phone);
            checkoutPage.shippingMethodRadio.first().check();
        } else {
            System.out.println("Info: Shipping details are already filled.");
        }
        return this;
    }

    public MagentoCheckoutSteps clickNextToReviewPayments() {
        checkoutPage.nextButton.click();
        checkoutPage.placeOrderButton.waitFor();
        System.out.println("Step: Proceeded to Review & Payments.");
        return this;
    }

    public MagentoCheckoutSteps validateShippingCostIsAddedToTotal() {
        String shippingCostText = checkoutPage.shippingCost.last().textContent().replaceAll("[^0-9.]", "");
        String orderTotalText = checkoutPage.orderTotal.textContent().replaceAll("[^0-9.]", "");

        double shippingCost = Double.parseDouble(shippingCostText);
        double orderTotal = Double.parseDouble(orderTotalText);

        System.out.println("Verification: Shipping cost is $" + shippingCost + ", Order total is $" + orderTotal);
        Assert.assertTrue(orderTotal > 0, "Order total should be greater than 0.");
        Assert.assertTrue(shippingCost >= 0, "Shipping cost should not be negative.");
        Assert.assertTrue(orderTotal >= shippingCost, "Order total should be greater than the shipping cost.");
        return this;
    }

    public MagentoCheckoutSteps validateAccountInfoIsCorrect(String... expectedDetails) {
        String addressDetails = checkoutPage.billingAddressDetails.textContent();
        for (String detail : expectedDetails) {
            Assert.assertTrue(addressDetails.contains(detail), "Address details should contain: " + detail);
        }
        System.out.println("Verification: Account information is correct on the review page.");
        return this;
    }

    public MagentoCheckoutSteps applyInvalidDiscountCodeAndVerifyError() {
        checkoutPage.applyDiscountLink.click();
        checkoutPage.discountCodeInput.fill("INVALID_CODE");
        checkoutPage.applyDiscountButton.click();
        assertThat(checkoutPage.discountError).isVisible();

        assertThat(checkoutPage.discountError).containsText("The coupon code isn't valid. Verify the code and try again.");
        return this;
    }

    public MagentoCheckoutSteps placeOrder() {
        checkoutPage.placeOrderButton.click();
        System.out.println("Step: Placed the order.");
        return this;
    }

    public MagentoCheckoutSteps verifyOrderSuccess() {
        assertThat(successPage.pageTitle).isVisible();
        assertThat(page).hasTitle("Success Page");

        String orderNumber = successPage.orderNumber.textContent();
        Assert.assertNotNull(orderNumber, "Order number should be displayed.");
        Assert.assertTrue(orderNumber.matches("\\d+"), "Order number should be a sequence of digits.");

        System.out.println("Verification: Order successful. Order number is: " + orderNumber);
        return this;
    }
}