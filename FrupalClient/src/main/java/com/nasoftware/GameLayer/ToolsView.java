package com.nasoftware.GameLayer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ToolsView extends JPanel {
    void render(LinkedList<String> tools) {
        this.removeAll();
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Your tools:");
        titleLabel.setBounds(20, 170, 150, 30);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setForeground(Color.BLUE);
        this.add(titleLabel);

        int i=0;
        for(String x:tools) {
            JLabel jLabel = new JLabel();
            jLabel.setText(x);
            jLabel.setBounds(20,200 + 30*i, 150, 30);
            jLabel.setHorizontalAlignment(JLabel.LEFT);
            this.add(jLabel);
            ++i;
        }
        this.repaint();
    }
}
