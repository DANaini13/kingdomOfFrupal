package com.company.Game;

import javax.swing.*;
import java.awt.*;

public class GameStatusView extends JPanel {
    private JLabel energyLabel;


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
        this.energyLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.add(energyLabel);
    }

    public void setEnergy(int energy) {
        this.energyLabel.setText("Energy Left: " + String.valueOf(energy));
        this.energyLabel.setForeground(Color.BLUE);
    }
}
