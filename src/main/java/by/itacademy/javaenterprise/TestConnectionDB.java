package by.itacademy.javaenterprise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class TestConnectionDB {

    private static final String USER_PROPERTY = "xxx";
    private static final String PASSWORD_PROPERTY = "xxx";
    private static final String URL_PROPERTY = "jdbc:mysql://localhost:3306/Athlete_Diary?useSSL=false";
    private static final String DRIVER_PROPERTY = "com.mysql.jdbc.Driver";
    private static final Logger logger = LoggerFactory.getLogger(
            TestConnectionDB.class);

    public static void setConnection() {
        logger.info("Just a log message.");
        try {
            Class.forName(DRIVER_PROPERTY);

        } catch (ClassNotFoundException e) {
            logger.error("Can't get class. No driver found.", e);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL_PROPERTY, USER_PROPERTY, PASSWORD_PROPERTY);
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
        setConnection();

    }

}

