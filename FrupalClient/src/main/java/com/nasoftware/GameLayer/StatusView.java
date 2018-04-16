package com.nasoftware.GameLayer;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {

    void render(Player player) {
        this.removeAll();
        int x = player.x;
        int y = player.y;
        int energy = player.energy;
        int wealth = player.wealth;
        JLabel position = new JLabel();
        position.setBounds(0, 10, 100, 20);
        position.setForeground(Color.BLUE);
        position.setText("Position: " + x + ", " + y);
        this.add(position);

        JLabel energyLabel = new JLabel();
        energyLabel.setBounds(0, 30, 100, 20);
        energyLabel.setForeground(Color.BLUE);
        energyLabel.setText("Energy: " + energy);
        this.add(energyLabel);

        JLabel wealthLabel = new JLabel();
        wealthLabel.setBounds(0, 50, 100, 20);
        wealthLabel.setForeground(Color.BLUE);
        wealthLabel.setText("Wealth: " + wealth);
        this.add(wealthLabel);
        this.repaint();
    }
}
