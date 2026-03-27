import java.util.List;

public class MCQ {
    private final String question;
    private final List<String> options;
    private final String answer;

    public MCQ(String question, List<String> options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }


    public List<String> getOptions() {
        return options;
    }


    public String getAnswer() {
        return answer;
    }

}
