package dao;

import java.sql.*;

public class ExamDAO {
    public int insertExam(int subject_id)throws SQLException {
        String query ="INSERT INTO exams (subject_id) VALUES (?)";
        try(Connection con =MySqlConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
                    ))
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
