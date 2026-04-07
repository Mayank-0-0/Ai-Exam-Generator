package ui;

import backend.ExamGenerator;
import model.MCQ;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class FormEditor extends JFrame {

    private List<MCQ> questions;
    private int index = 0;
    private String subject;

    private JTextArea questionArea;
    private JTextField optA, optB, optC, optD;
    private JComboBox<String> answerBox;

    public FormEditor(List<MCQ> list, String subject) {
        this.questions = new ArrayList<>(list);
        this.subject = subject;

        setTitle("Edit Questions");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        questionArea = new JTextArea(4, 40);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);

        optA = new JTextField();
        optB = new JTextField();
        optC = new JTextField();
        optD = new JTextField();

        answerBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});

        panel.add(new JLabel("Question:"));
        panel.add(new JScrollPane(questionArea));

        panel.add(new JLabel("Option A:"));
        panel.add(optA);

        panel.add(new JLabel("Option B:"));
        panel.add(optB);

        panel.add(new JLabel("Option C:"));
        panel.add(optC);

        panel.add(new JLabel("Option D:"));
        panel.add(optD);

        panel.add(new JLabel("Answer:"));
        panel.add(answerBox);


        JButton prevBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");
        JButton saveBtn = new JButton("Save All");

        JPanel btnPanel = new JPanel();
        btnPanel.add(prevBtn);
        btnPanel.add(nextBtn);
        btnPanel.add(saveBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadQuestion(index);


        prevBtn.addActionListener(_ -> {
            saveCurrent();
            if (index > 0) {
                index--;
                loadQuestion(index);
            }
        });

        nextBtn.addActionListener(e -> {
            saveCurrent();
            if (index < questions.size() - 1) {
                index++;
                loadQuestion(index);
            }
        });

        saveBtn.addActionListener(e -> {
            saveCurrent();
            try {
                new ExamGenerator().dbInsertion(questions, subject);
                JOptionPane.showMessageDialog(this, "Saved to DB!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving");
            }
        });

        setVisible(true);
    }

    private void loadQuestion(int i) {
        MCQ q = questions.get(i);

        questionArea.setText("Q"+i+" ) "+q.getQuestion());

        optA.setText(q.getOptions().get(0));
        optB.setText(q.getOptions().get(1));
        optC.setText(q.getOptions().get(2));
        optD.setText(q.getOptions().get(3));

        answerBox.setSelectedItem(q.getAnswer());
    }

    private void saveCurrent() {
        List<String> options = new ArrayList<>();
        options.add(optA.getText());
        options.add(optB.getText());
        options.add(optC.getText());
        options.add(optD.getText());

        MCQ updated = new MCQ(
                questionArea.getText(),
                options,
                answerBox.getSelectedItem().toString()
        );

        questions.set(index, updated);
    }
}