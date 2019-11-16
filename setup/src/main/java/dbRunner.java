import java.sql.*;

public class dbRunner {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:db/rta.db");
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

            }
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.execute("drop table if exists accounts");
            statement.execute("create table accounts (id integer, name string)");

            statement.execute("insert into accounts (id, name) values (1, 'name') ");

            ResultSet resultSet = statement.executeQuery("select * from accounts");
            resultSet.next();


//            statement.executeUpdate("drop table if exists requests");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
