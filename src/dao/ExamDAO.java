package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExamDAO {
    public int insertExam(int subject_id)throws SQLException {
        String query ="INSERT INTO exams (subject_id) VALUES (?)";
        try(Connection con =MySqlConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(query))
        {
            ps.setInt(1,subject_id);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys())
            {
                if(rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }return -1;
    }
}
