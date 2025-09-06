package ge.tbc.testautomation.data;
import ge.tbc.testautomation.steps.DatabaseSteps;
import org.testng.annotations.DataProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
public class RegistrationDataProvider {
    @DataProvider(name = "registrationDbData")
    public static Object[][] getRegistrationDataFromDb() {
        DatabaseSteps dbSteps = new DatabaseSteps();
        ResultSet resultSet = null;

        try {
            resultSet = dbSteps.getAllRegistrationData();

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();


            int columnCount = resultSet.getMetaData().getColumnCount();

            Object[][] data = new Object[rowCount][columnCount];

            int rowIndex = 0;
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    data[rowIndex][i] = resultSet.getObject(i + 1);
                }
                rowIndex++;
            }
            return data;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch data from the database for DataProvider.", e);
        } finally {
            if (resultSet != null) {
                try {

                    resultSet.getStatement().getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
