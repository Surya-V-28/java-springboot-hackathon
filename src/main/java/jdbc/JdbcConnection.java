package jdbc;

import java.sql.*;

public class JdbcConnection {
    public static void main(String[] args) {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Correct JDBC URL
            String url = "jdbc:mysql://localhost:3306/regschema";
            String username = "root";
            String password = "Thina";

            // Establishing the connection
            Connection con = DriverManager.getConnection(url, username, password);

            // Creating a statement
            Statement st = con.createStatement();

            // SQL to create a table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL, "
                    + "email VARCHAR(225) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL"
                    + ");";
            st.execute(createTableSQL);
            System.out.println("Table created successfully!");

            // we will modify this later by inserting the values entered by user
            String insertDataSQL = "INSERT INTO users (username,email, password) "
                    + "VALUES ('john_doe','this@gmail.com', 'password123'), "
                    + "('jane_smith','that@gmail.com', 'password456');";
            st.executeUpdate(insertDataSQL);
            System.out.println("Data inserted successfully!");

            // Execute query to select the data
            ResultSet rs = st.executeQuery("SELECT * FROM users");


            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("email:" + rs.getString("email"));
                System.out.println("Password: " + rs.getString("password"));

            }

            // Close the connection
            rs.close();
            st.close();
            con.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred.");
            e.printStackTrace();
        }
    }
}
