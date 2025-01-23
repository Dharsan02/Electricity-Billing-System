package electricity.billing.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class database {
    Connection connection;
    Statement statement;

    public database() {
        try {
            // Establish the connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bill" ,"root", "");

            // Create the statement
            statement = connection.createStatement();

            // Print a success message
            System.out.println("Connection to the database established successfully.");
        } catch (Exception e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }
}
