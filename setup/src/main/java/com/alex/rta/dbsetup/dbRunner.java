package com.alex.rta.dbsetup;
import java.sql.*;

public class dbRunner {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:rta.db");
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

            }

            new create(connection).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
