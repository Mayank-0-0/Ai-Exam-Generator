package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExamQuestionDAO {
    public void mapExamToQuestion(int exam_id,int question_id)throws SQLException{
        String query = "INSERT INTO exam_questions (exam_id, question_id) VALUES (?, ?)";
        try(Connection con = MySqlConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(query)){
            ps.setInt(1,exam_id);
            ps.setInt(2,question_id);
            ps.executeUpdate();
        }
    }
}
