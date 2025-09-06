package ge.tbc.testautomation.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RegistrationPage {
    public final Locator firstNameInput;
    public final Locator lastNameInput;
    public final Locator genderMaleRadio;
    public final Locator genderFemaleRadio;
    public final Locator modelDropdown;
    public final Locator address1Input;
    public final Locator address2Input;
    public final Locator cityInput;
    public final Locator contact1Input;
    public final Locator contact2Input;

    public RegistrationPage(Page page) {

        this.firstNameInput = page.locator("input[value='First Name']");
        this.lastNameInput = page.locator("input[value='Last Name']");
        this.genderMaleRadio = page.locator("input[name='gender'][value='male']");
        this.genderFemaleRadio = page.locator("input[name='gender'][value='female']");
        this.modelDropdown = page.locator("select[name='model']");
        this.address1Input = page.locator("input[value='Address1']");
        this.address2Input = page.locator("input[value='Address2']");
        this.cityInput = page.locator("input[value='City']");
        this.contact1Input = page.locator("input[value='Contact1']");
        this.contact2Input = page.locator("input[value='Contact2']");
    }
}