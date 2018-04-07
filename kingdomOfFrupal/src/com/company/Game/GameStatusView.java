package com.company.Game;

import javax.swing.*;
import java.awt.*;

public class GameStatusView extends JPanel {
    private JLabel energyLabel;
    private JLabel locationLabel;
    private JLabel wealthLabel;


    static public GameStatusView create(int x, int y, int w, int h) {
        GameStatusView gameStatusView = new GameStatusView(x, y, w, h);
        gameStatusView.setLayout(null);
        return gameStatusView;
    }

    private GameStatusView(int x, int y, int w, int h) {
        this.setBounds(x, y, w, h);
        this.initSubviews();
    }

    private void initSubviews() {
        this.energyLabel = new JLabel("");
        this.energyLabel.setBounds(0, 0, this.getWidth(), this.getHeight()/3);
        this.add(energyLabel);

        this.locationLabel = new JLabel("");
        this.locationLabel.setBounds(0, this.getHeight()/3, this.getWidth(), this.getHeight()/3);
        this.add(locationLabel);

        this.wealthLabel = new JLabel("");
        this.wealthLabel.setBounds(0, this.getHeight()/3 * 2, this.getWidth(), this.getHeight()/3);
        this.add(wealthLabel);
    }

    public void setEnergy(int energy) {
        this.energyLabel.setText("Energy Left: " + String.valueOf(energy));
        this.energyLabel.setForeground(Color.BLUE);
    }

    public void setLocation(int x, int y) {
        this.locationLabel.setText("Location: (" + String.valueOf(x) + ", " + String.valueOf(y) + ")");
        this.locationLabel.setForeground(Color.BLUE);
    }

    public void setWealth(int wealth) {
        this.wealthLabel.setText("Wealth: " + String.valueOf(wealth));
        this.wealthLabel.setForeground(Color.BLUE);
    }
}
