package ge.tbc.testautomation.data;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnection {
    private static final String PROPERTIES_FILE_NAME = "database.properties";
    private static final Properties properties = new Properties();

    static {

        try (InputStream inputStream = SQLConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            if (inputStream == null) {
                throw new RuntimeException("FATAL ERROR: Could not find " + PROPERTIES_FILE_NAME + " in the classpath.");
            }
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("FATAL ERROR: Could not load " + PROPERTIES_FILE_NAME, e);
        }
    }

    public static Connection connect() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new SQLException("Database properties (url, username, password) not found in " + PROPERTIES_FILE_NAME);
        }

        return DriverManager.getConnection(url, user, password);
    }
}