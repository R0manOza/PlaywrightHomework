package ge.tbc.testautomation.steps;


import ge.tbc.testautomation.data.SQLConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSteps {

    public ResultSet getAllRegistrationData() throws SQLException {

        Connection connection = SQLConnection.connect();

        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String sqlQuery = "SELECT * FROM RegistrationData";

        return statement.executeQuery(sqlQuery);
    }
}