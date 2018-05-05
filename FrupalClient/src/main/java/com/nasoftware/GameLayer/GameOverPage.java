package com.nasoftware.GameLayer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class GameOverPage extends JPanel {
    static private GameOverPage gameOverPage;

    static public GameOverPage generateGameOverPage(String winner, LinkedList<Player> players) {
        if(gameOverPage == null) {
            JFrame frame = new JFrame();
            gameOverPage = new GameOverPage();
            gameOverPage.setLayout(null);
            gameOverPage.setBounds(0, 0, 500, 300);
            frame.setSize(500, 300);
            frame.setResizable(false);
            frame.setContentPane(gameOverPage);
            frame.setVisible(true);
            frame.setTitle("Game Over");
            gameOverPage.initViews(winner, players);
        }
        return gameOverPage;
    }

    static public GameOverPage generateGameOverPage() {
        if(gameOverPage == null) {
            JFrame frame = new JFrame();
            gameOverPage = new GameOverPage();
            gameOverPage.setLayout(null);
            gameOverPage.setBounds(0, 0, 500, 300);
            frame.setSize(500, 300);
            frame.setResizable(false);
            frame.setContentPane(gameOverPage);
            frame.setVisible(true);
            frame.setTitle("Game Over");
            gameOverPage.initViews();
        }
        return gameOverPage;
    }

    void initViews(String winner, LinkedList<Player> players) {
        if(winner != null) {
            JLabel nameLabel = new JLabel();
            nameLabel.setBounds(0, 150, 500, 30);
            nameLabel.setText("Winner is: " + winner);
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(nameLabel);
        }
        JLabel congLabel = new JLabel();
        congLabel.setBounds(0, 0, 500, 150);
        congLabel.setText("GAME OVER!!!");
        congLabel.setForeground(Color.RED);
        congLabel.setVerticalAlignment(JLabel.CENTER);
        congLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(congLabel);
    }

    void initViews() {
         JLabel congLabel = new JLabel();
        congLabel.setBounds(0, 0, 500, 150);
        congLabel.setText("SORRY, YOU WERE DEAD BECAUSE YOU WERE OUT OF ENERGY!!!");
        congLabel.setForeground(Color.RED);
        congLabel.setVerticalAlignment(JLabel.CENTER);
        congLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(congLabel);
    }
}
