package dao;

import java.sql.*;

public class SubjectDAO {
    public int insertSubject(String subject)throws SQLException {
        try(Connection con=MySqlConnection.getConnection())
        {
            String query="SELECT subject_id FROM subject WHERE subject_name = ?";
            PreparedStatement ps1 =con.prepareStatement(query);
            ps1.setString(1,subject);
            ResultSet rs1= ps1.executeQuery();
            if(rs1.next()){
                return rs1.getInt("subject_id");
            }

            String insert = "INSERT INTO subject(subject_name) VALUES(?)";
            PreparedStatement ps2 = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps2.setString(1, subject);
            ps2.executeUpdate();
            try(ResultSet rs2=ps2.getGeneratedKeys())
            {
                if(rs2.next())
                {
                    return rs2.getInt(1);
                }
            }
        }return -1;
    }
}
