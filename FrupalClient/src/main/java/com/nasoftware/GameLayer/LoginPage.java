package com.nasoftware.GameLayer;

import com.nasoftware.LogicLayer.AccountService;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JPanel {
    static private LoginPage loginPage = null;

    static public LoginPage getLoginPage() {
        if(loginPage == null) {
            JFrame frame = new JFrame();
            loginPage = new LoginPage(frame);
            loginPage.setLayout(null);
            loginPage.setBounds(0, 0, 300, 500);
            frame.setSize(300, 500);
            frame.setResizable(false);
            frame.setContentPane(loginPage);
            loginPage.initPage();
            frame.setVisible(true);
            frame.setTitle("Kingdom of Frupal");
        }
        return loginPage;
    }

    private JFrame frame;

    LoginPage(JFrame frame) {
        this.frame = frame;
    }

    void initPage() {
        JLabel jLabel = new JLabel();
        jLabel.setText("Ready to play?");
        jLabel.setBounds(50, 100, 200, 30);
        this.add(jLabel);

        JButton logInButton = new JButton();
        JTextField accountTextField = new JTextField("input the name here");
        accountTextField.setBounds(50, 200, 200, 30);

        this.add(accountTextField);

        logInButton.setText("Login");
        logInButton.setBounds(100, 300, 100, 30);
        JFrame currentFrame = this.frame;
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(accountTextField.getText().equals("input the name here") ||
                        accountTextField.getText().equals(""))
                    return;
                AccountService accountService = AccountService.getAccountService();
                accountService.login(accountTextField.getText(), (response) -> {
                    try {
                        if(response.getInt("error") == 0) {
                            accountService.myAccount = accountTextField.getText();
                            currentFrame.dispose();
                        }

                    } catch (JSONException e1) {
                        try {
                            jLabel.setText(response.getString("error"));
                            jLabel.setForeground(Color.RED);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
        });
        this.add(logInButton);
    }

}
