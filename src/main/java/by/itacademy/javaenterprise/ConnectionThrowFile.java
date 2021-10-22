package by.itacademy.javaenterprise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class ConnectionThrowFile {

    private static final Logger logger = LoggerFactory.getLogger(
            ConnectionThrowFile.class);
    private Properties props = new Properties();

    {
        try (InputStream in = Files.newInputStream((Paths.get("src/main/resources/db.properties")))) {
            props.load(in);
        } catch (IOException e) {
            logger.info("Error with reading File db.properties." + e);
        }

    }

    private String url = props.getProperty("url");
    private String username = props.getProperty("user");
    private String password = props.getProperty("password");
    private String driver = props.getProperty("driver");

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getDriver() {
        return driver;
    }

    public void setConnection() {
        logger.info("Just a log message.");

        try {
            Class.forName(driver);

        } catch (ClassNotFoundException e) {
            logger.error("Can't get class. No driver found.", e);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Connection to Athlete_Diary succesfull!");
        } catch (SQLException e) {
            logger.error("Can't get connection. Incorrect URL" + e);
        }
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate("INSERT INTO `Athlete_Diary`.`Exercises_on_training` " +
                    "(`Training_id`,`Name_Exercises_id`,`Total_repeats`,`Total_time`,`Total_weight`,`Total_approaches`)" +
                    " VALUES (3, 9, 12, '00:02:01', 80, 1)");

            ResultSet rs = statement.executeQuery("SELECT Name FROM Exercises_on_training " +
                    "Left Join Exercises_name ON Exercises_on_training.Name_Exercises_id = Exercises_name.Name_Exercises_id " +
                    "WHERE Total_repeats <40 AND Total_weight >1 Limit 4 Offset 1");
            while (rs.next()) {
                String result = rs.getString(1);
                logger.info("Result of the query {} .", result);
            }
            try {
                rs.close();
            } catch (SQLException throwables) {
                logger.error("Can't close ResultSet" + throwables);
            }

        } catch (SQLException throwables) {
            logger.error("Can't execute query" + throwables);
        }


        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Can't close connection" + e);
        }
    }


    public static void main(String[] args) {
        ConnectionThrowFile connectionThrowFile = new ConnectionThrowFile();
        connectionThrowFile.setConnection();

    }
}
