package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainLayout extends JFrame{
    public MainLayout(){
        this.setTitle("AI-Exam generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new GridBagLayout());

        this.setVisible(true);
    }
}
