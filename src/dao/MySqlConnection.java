package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MySqlConnection {
    private static final String url ="jdbc:mysql://localhost:3306/";
    private static final String username = "your_username";
    private static final String pass ="your_password";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,pass);
    }
}
