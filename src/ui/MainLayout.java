package ui;
import backend.ExamGenerator;
import model.MCQ;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;


public class MainLayout extends JFrame implements ActionListener {
    public JButton generate_button;
    JTextField subject_textField,topic_textField,numberOfQuestion_textField;
    public MainLayout(){
        this.setTitle("AI-Exam generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        JPanel inputPanel=new JPanel();
        inputPanel.setLayout(new GridLayout(3,2,10,10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Exam Details"));

        generate_button = new JButton("Submit");
        generate_button.addActionListener(this);

        subject_textField = new JTextField();
        subject_textField.setPreferredSize(new Dimension(250,25));

        topic_textField = new JTextField();
        topic_textField.setPreferredSize(new Dimension(250,25));

        numberOfQuestion_textField = new JTextField();
        numberOfQuestion_textField.setPreferredSize(new Dimension(250,25));

        inputPanel.add(new JLabel("Enter Subject"));
        inputPanel.add(subject_textField);
        inputPanel.add(new JLabel("Enter Topic/Chapter name"));
        inputPanel.add(topic_textField);
        inputPanel.add(new JLabel("Number of question"));
        inputPanel.add(numberOfQuestion_textField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generate_button);
        this.add(inputPanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==generate_button){
            String subject=subject_textField.getText().toLowerCase();
            String topic=topic_textField.getText().toLowerCase();
            int noq=Integer.parseInt(numberOfQuestion_textField.getText());
            ExamGenerator exam=new ExamGenerator();
            List<MCQ> mcqlist;
            try {
                mcqlist=exam.generateExam(subject,topic,noq);
                new FormEditor(mcqlist,subject);
            } catch (IOException | InterruptedException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
   