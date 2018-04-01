package com.company.Game;
import javax.swing.*;

public class GameMainView extends JPanel {

    private final JFrame frame;

    static public GameMainView createAndShowView(int screenW, int screenH) {
        JFrame frame = new JFrame("Kingdom of Frupal");
        frame.setLayout(null);
        frame.setBounds(0, 0, screenW, screenH);
        GameMainView gameMainView = new GameMainView(frame);
        frame.setContentPane(gameMainView);
        frame.setResizable(false);
        frame.setVisible(true);
        return gameMainView;
    }

    public GameMainView(JFrame frame) {
        this.frame = frame;
        this.setBounds(frame.getBounds());
        this.setLayout(null);
        initSubviews();
    }

    private void initSubviews() {
        GameView gameView = GameView.createWithBound(200, 100, 500, 500);
        this.add(gameView);
    }

}
