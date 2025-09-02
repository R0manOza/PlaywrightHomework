package ge.tbc.testautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MagentoCheckoutPage {
    private final Page page;

    public final Locator streetAddressInput;
    public final Locator cityInput;
    public final Locator regionDropdown;
    public final Locator postCodeInput;
    public final Locator countryDropdown;
    public final Locator telephoneInput;
    public final Locator shippingMethodRadio;
    public final Locator nextButton;

    public final Locator billingAddressDetails;
    public final Locator shippingAddressDetails;
    public final Locator shippingCost;
    public final Locator orderTotal;
    public final Locator applyDiscountLink;
    public final Locator discountCodeInput;
    public final Locator applyDiscountButton;
    public final Locator discountError;
    public final Locator placeOrderButton;


    public MagentoCheckoutPage(Page page) {
        this.page = page;
        this.streetAddressInput = page.locator("input[name='street[0]']");
        this.cityInput = page.locator("input[name='city']");
        this.regionDropdown = page.locator("select[name='region_id']");
        this.postCodeInput = page.locator("input[name='postcode']");
        this.countryDropdown = page.locator("select[name='country_id']");
        this.telephoneInput = page.locator("input[name='telephone']");
        this.shippingMethodRadio = page.locator(".radio");
        this.nextButton = page.locator("button.button.action.continue.primary");

        this.billingAddressDetails = page.locator(".billing-address-details");
        this.shippingAddressDetails = page.locator(".shipping-information-content");
        this.shippingCost = page.locator("td.amount span.price"); // For "Shipping & Handling"
        this.orderTotal = page.locator("tr.grand.totals span.price");
        this.applyDiscountLink = page.locator("#block-discount-heading");
        this.discountCodeInput = page.locator("#discount-code");
        this.applyDiscountButton = page.locator("button.action.action-apply");
        this.discountError = page.locator(".message-error.error.message");
        this.placeOrderButton = page.locator("button.action.primary.checkout");
    }
}