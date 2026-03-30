package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubjectDAO {
    public void insertSubject(String subject)throws SQLException {
        String query="INSERT INTO subject (subject_name) VALUES (?)";
        try(Connection con=MySqlConnection.getConnection();
        PreparedStatement ps =con.prepareStatement(query))
        {
            ps.setString(1,subject);
            ps.executeUpdate();
        }
    }
}
