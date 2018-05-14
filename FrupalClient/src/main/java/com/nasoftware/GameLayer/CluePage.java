package com.nasoftware.GameLayer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CluePage extends JPanel {
    static private CluePage cluePage = null;
    static public synchronized CluePage getCluePage() {
        if(cluePage == null) {
            JFrame frame = new JFrame();
            cluePage = new CluePage(frame);
            cluePage.setLayout(null);
            cluePage.setBounds(0, 0, 500, 300);
            frame.setSize(500, 300);
            frame.setResizable(false);
            frame.setContentPane(cluePage);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    super.windowClosed(windowEvent);
                    cluePage = null;
                }
            });
            cluePage.initPage();
            frame.setVisible(true);
            frame.setTitle("Kingdom of Frupal");
        }
        return cluePage;
    }

    CluePage(JFrame frame) {
        this.frame = frame;
    }

    private void initPage() {
        JLabel jLabel = new JLabel();
        jLabel.setText("you got a clue:");
        jLabel.setBounds(20, 20, 460, 30);
        this.add(jLabel);
        jTextArea = new JTextArea();
        jTextArea.setBounds(20, 80, 460, 230);
        this.add(jTextArea);
    }

    public void setClue(String message) {
        jTextArea.setText(message);
    }

    private JFrame frame;
    private JTextArea jTextArea;
}
