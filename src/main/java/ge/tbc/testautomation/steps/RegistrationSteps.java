package ge.tbc.testautomation.steps;
import com.microsoft.playwright.Page;
import ge.tbc.testautomation.pages.RegistrationPage;
import io.qameta.allure.Step;
public class RegistrationSteps {
    private final Page page;
    private final RegistrationPage registrationPage;

    public RegistrationSteps(Page page) {
        this.page = page;
        this.registrationPage = new RegistrationPage(page);
    }

    @Step("Navigate to the registration page")
    public RegistrationSteps navigateToRegistrationForm() {
        page.navigate("https://techcanvass.com/examples/register.html");
        registrationPage.firstNameInput.waitFor();
        return this;
    }

    @Step("Fill First Name with: {firstName}")
    public RegistrationSteps fillFirstName(String firstName) {
        registrationPage.firstNameInput.fill(firstName);
        return this;
    }

    @Step("Fill Last Name with: {lastName}")
    public RegistrationSteps fillLastName(String lastName) {
        registrationPage.lastNameInput.fill(lastName);
        return this;
    }

    @Step("Select Gender: {gender}")
    public RegistrationSteps selectGender(String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            registrationPage.genderMaleRadio.click();
        } else if ("female".equalsIgnoreCase(gender)) {
            registrationPage.genderFemaleRadio.click();
        }
        return this;
    }

    @Step("Select Model: {model}")
    public RegistrationSteps selectModel(String model) {
        registrationPage.modelDropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(model));
        return this;
    }

    @Step("Fill Address Line 1 with: {address1}")
    public RegistrationSteps fillAddress1(String address1) {
        registrationPage.address1Input.fill(address1);
        return this;
    }

    @Step("Fill Address Line 2 with: {address2}")
    public RegistrationSteps fillAddress2(String address2) {
        registrationPage.address2Input.fill(address2);
        return this;
    }

    @Step("Fill City with: {city}")
    public RegistrationSteps fillCity(String city) {
        registrationPage.cityInput.fill(city);
        return this;
    }

    @Step("Fill Contact 1 with: {contact1}")
    public RegistrationSteps fillContact1(String contact1) {
        registrationPage.contact1Input.fill(contact1);
        return this;
    }

    @Step("Fill Contact 2 with: {contact2}")
    public RegistrationSteps fillContact2(String contact2) {
        registrationPage.contact2Input.fill(contact2);
        return this;
    }

    @Step("Refresh the page to clear the form")
    public RegistrationSteps refreshPage() {
        page.reload();
        registrationPage.firstNameInput.waitFor();
        return this;
    }
}
