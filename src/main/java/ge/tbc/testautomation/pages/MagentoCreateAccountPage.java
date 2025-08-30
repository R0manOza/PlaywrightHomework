package ge.tbc.testautomation.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
public class MagentoCreateAccountPage {
    private final Page page;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator confirmPasswordInput;
    private final Locator createAccountButton;
    public MagentoCreateAccountPage(Page page) {
        this.page = page;
        this.firstNameInput = page.locator("#firstname");
        this.lastNameInput = page.locator("#lastname");
        this.emailInput = page.locator("#email_address");
        this.passwordInput = page.locator("#password");
        this.confirmPasswordInput = page.locator("#password-confirmation");
        this.createAccountButton = page.locator("button.action.submit.primary");
    }
    public void registerUser(String firstName, String lastName, String email, String password) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        emailInput.fill(email);
        passwordInput.fill(password);
        confirmPasswordInput.fill(password);
        createAccountButton.click();
    }
}
