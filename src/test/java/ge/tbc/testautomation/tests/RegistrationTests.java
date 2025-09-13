package ge.tbc.testautomation.tests;
import ge.tbc.testautomation.data.RegistrationDataProvider;
import ge.tbc.testautomation.steps.RegistrationSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

    @Epic("Data Driven Tests")
    @Feature("JDBC Integration")
    public class RegistrationTests extends BaseTest{

        private RegistrationSteps registrationSteps;

        @BeforeClass
        public void initializeSteps() {
            registrationSteps = new RegistrationSteps(page);
        }

        @Test(
                dataProvider = "registrationDbData",
                dataProviderClass = RegistrationDataProvider.class,
                description = "Fill the registration form using data from the database."
        )
        @Severity(SeverityLevel.NORMAL)
        @Description("This test iterates through each record in the 'RegistrationData' database table. " +
                "For each record, it fills the entire web form and then refreshes the page to prepare for the next iteration.")
        public void fillFormWithDbData(int id, String firstName, String lastName, String gender, String model,
                                       String address1, String address2, String city,
                                       String contact1, String contact2) {

                  registrationSteps.navigateToRegistrationForm()
                    .fillFirstName(firstName)
                    .fillLastName(lastName)
                    .selectGender(gender)
                    .selectModel(model)
                    .fillAddress1(address1)
                    .fillAddress2(address2)
                    .fillCity(city)
                    .fillContact1(contact1)
                    .fillContact2(contact2)
                    .refreshPage();
        }
}
