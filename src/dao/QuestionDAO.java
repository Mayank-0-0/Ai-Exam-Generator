package dao;
import java.sql.*;
import java.util.*;
public class QuestionDAO {
    public void insertQuestion(List<Question> quest )throws SQLException{
        String query = "INSERT INTO questions (question,option_A,option_B,option_C,option_D,answer,subject_id)VALUES (?,?,?,?,?,?,?)";
        try(Connection con=MySqlConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query)){
            for(Question q:quest){
                ps.setString(1,q.getQuestion_Text());
                ps.setString(2, q.getOptionA());
                ps.setString(3, q.getOptionB());
                ps.setString(4,q.getOptionC());
                ps.setString(5, q.getOptionD());
                ps.setString(6, q.getAnswer());
                ps.setInt(7,q.getSubjectID());
                ps.addBatch(query);
            }ps.executeBatch();
        }
    }
}
