package com.nasoftware.GameLayer;
import javax.swing.*;
import java.awt.*;

public class MessageView extends JPanel {
    static public JTextField jTextField = new JTextField();
    private JLabel messageLabel = new JLabel();

    void init() {
        jTextField.setBounds(0, 10, 180, 50);
        this.add(jTextField);
        messageLabel.setBounds(0, 60, 180, 30);
        messageLabel.setForeground(Color.BLUE);
        this.add(messageLabel);
    }

    void showMessage(String message) {
        this.messageLabel.setText(message);
    }

}
