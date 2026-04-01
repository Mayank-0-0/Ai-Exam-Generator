import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.MCQ;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        InputStreamReader in =new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);

        System.out.println("Enter your subject :");
        String subject = bf.readLine();
        String lwr_subject=subject.toLowerCase();

        System.out.println("Enter topic or chapter name :");
        String topic = bf.readLine();

        System.out.println("Enter number of question :");
        String input = bf.readLine();
        int noOfQues = Integer.parseInt(input);

        String prompt = """
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
                        """.formatted(subject, noOfQues, topic);

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

        JsonObject root=JsonParser.parseString((response.body())).getAsJsonObject();
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
        List<Question> ques =new ArrayList<>();

        QuestionDAO con1 = new QuestionDAO();
        SubjectDAO con2 = new SubjectDAO();
        ExamDAO con3 = new ExamDAO();
        ExamQuestionDAO con4 = new ExamQuestionDAO();

        int subjectKey;
        subjectKey=con2.insertSubject(lwr_subject);
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

        for(int i=0;i<mcqlist.size();i++)
        {
            MCQ q=mcqlist.get(i);
            System.out.println((i+1)+") "+q.getQuestion()+"\n");
            for(int j=0;j<q.getOptions().size();j++)
            {
                System.out.println((char)(65+j) +"-> "+q.getOptions().get(j));
            }
            System.out.println("Answer :"+q.getAnswer());
            System.out.println("\n");
        }
    }
}