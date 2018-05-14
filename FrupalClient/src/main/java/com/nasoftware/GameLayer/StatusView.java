package com.nasoftware.GameLayer;

import com.nasoftware.LogicLayer.AccountService;
import com.nasoftware.LogicLayer.MessageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

public class StatusView extends JPanel {

    void render(LinkedList<Player> playerList) {
        this.removeAll();



        Iterator it = playerList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Player player = (Player) it.next();
            int x = player.x;
            int y = player.y;
            int energy = player.energy;
            int wealth = player.wealth;
            JLabel nameLabel = new JLabel();
            nameLabel.setBounds(i * 120, 10, 120, 20);
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setText("name:" + player.account);
            this.add(nameLabel);

            JLabel position = new JLabel();
            position.setBounds(i * 120, 30, 120, 20);
            position.setForeground(Color.BLUE);
            position.setText("Position: " + x + ", " + y);
            this.add(position);

            JLabel energyLabel = new JLabel();
            energyLabel.setBounds(i * 120, 50, 120, 20);
            energyLabel.setForeground(Color.BLUE);
            energyLabel.setText("Energy: " + energy);
            this.add(energyLabel);

            JLabel wealthLabel = new JLabel();
            wealthLabel.setBounds(i * 120, 70, 120, 20);
            wealthLabel.setForeground(Color.BLUE);
            wealthLabel.setText("Wealth: " + wealth);
            this.add(wealthLabel);

            if(player.account.equals(AccountService.myAccount)) {
                JButton jButton = new JButton("group");
                jButton.setBounds(i * 120, 90, 120, 20);
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(!MessageView.jTextField.getText().isEmpty()) {
                            MessageService.getMessageService().sendGroupMessage(MessageView.jTextField.getText());
                        }
                        GameViewController.recoverListener();
                    }
                });
                this.add(jButton);
            }else {
                JButton jButton = new JButton("text");
                jButton.setBounds(i * 120, 90, 120, 20);
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(!MessageView.jTextField.getText().isEmpty()) {
                            MessageService.getMessageService().sendPrivateMessage(MessageView.jTextField.getText(), player.account);
                        }
                        GameViewController.recoverListener();
                    }
                });
                this.add(jButton);
            }
            ++i;
        }

        this.repaint();
    }
}
