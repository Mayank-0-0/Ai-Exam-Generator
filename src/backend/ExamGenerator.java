package backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dao.*;
import model.MCQ;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamGenerator {
    public List<MCQ> generateExam(String subject, String topic, int numberOfQues ,String description)throws IOException, InterruptedException, SQLException {
        String prompt;
        if(!description.isEmpty()) {
            prompt = """
                        You are a %s professor.

                        Generate exactly %d multiple-choice questions on the topic "%s".
                        More details about question "%s".
                        STRICT RULES:
                            - Output ONLY valid JSON.
                            - Do NOT include explanations, headings, or extra text.
                            - Do NOT include markdown or code blocks.
                            - Ensure all keys and values are properly quoted.
                            - Ensure valid JSON array format.

                        Format:
                        [
                            {
                                "question": "string",
                                "options": ["optionA","optionB","optionC","optionD"],
                                "answer": "A/B/C/D"
                            }
                        ]
                    """.formatted(subject, numberOfQues, topic, description);
        }else {
            prompt = """
                        You are a %s professor.

                        Generate exactly %d multiple-choice questions on the topic "%s".

                        STRICT RULES:
                            - Output ONLY valid JSON.
                            - Do NOT include explanations, headings, or extra text.
                            - Do NOT include markdown or code blocks.
                            - Ensure all keys and values are properly quoted.
                            - Ensure valid JSON array format.

                        Format:
                        [
                            {
                                "question": "string",
                                "options": ["optionA","optionB","optionC","optionD"],
                                "answer": "A/B/C/D"
                            }
                        ]
                    """.formatted(subject, numberOfQues, topic);
        }

        JsonObject body = new JsonObject();
        body.addProperty("model", "llama-3.1-8b-instant");

        JsonArray messages = new JsonArray();
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", prompt);

        messages.add(msg);
        body.add("messages", messages);

        String requestBody = new Gson().toJson(body);

        String key=System.getenv("GROQ_API_KEY");


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Authorization", "Bearer "+key)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());

        JsonObject root= JsonParser.parseString((response.body())).getAsJsonObject();
        String content = root
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
        content = content.trim();
        if(content.startsWith("```"))
        {
            content=content.replace("```json","")
                    .replace("```","")
                    .trim();
        }
        Gson gson= new Gson();
        List<MCQ> mcqlist=gson.fromJson(content,new TypeToken<List<MCQ>>(){}.getType());
        return mcqlist;
    }
    public void dbInsertion(List<MCQ> mcqlist,String subject) throws SQLException{
        List<Question> ques =new ArrayList<>();

        QuestionDAO con1 = new QuestionDAO();
        SubjectDAO con2 = new SubjectDAO();
        ExamDAO con3 = new ExamDAO();
        ExamQuestionDAO con4 = new ExamQuestionDAO();

        int subjectKey;
        subjectKey=con2.insertSubject(subject);
        for (MCQ q : mcqlist) {
            Question question = new Question();
            question.setQuestion_Text(q.getQuestion());
            question.setAnswer(q.getAnswer());
            question.setSubjectID(subjectKey);
            question.setOptionA(q.getOptions().get(0));
            question.setOptionB(q.getOptions().get(1));
            question.setOptionC(q.getOptions().get(2));
            question.setOptionD(q.getOptions().get(3));
            ques.add(question);
        }

        int examKey;

        List<Integer>ques_id=con1.insertQuestion(ques);
        examKey=con3.insertExam(subjectKey);

        for (Integer integer : ques_id) {
            con4.mapExamToQuestion(examKey, integer);
        }

    }
}

