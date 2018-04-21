package com.nasoftware.GameLayer;

import javax.swing.*;
import java.awt.*;
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
            ++i;
        }

        this.repaint();
    }
}
