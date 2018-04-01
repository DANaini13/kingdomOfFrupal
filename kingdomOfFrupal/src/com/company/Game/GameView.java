package com.company.Game;
import javax.swing.*;
import java.awt.*;

public class GameView extends Panel {

    static public GameView createWithBound(int x, int y, int w, int h) {
        GameView gameView = new GameView(x, y, w, h);
        gameView.setLayout(null);
        return gameView;
    }

    public GameView(int x, int y, int w, int h) {
        this.setBounds(x, y, w, h);
        initSubviews();
    }

    private void initSubviews() {
        JLabel lines[][] = new JLabel[20][20];
        int height = this.getBounds().height / 20;
        int width  = this.getBounds().width / 20;
        for (int i=0; i<20; ++i) {
            for (int j=0; j<20; ++j) {
                lines[i][j] = new JLabel("*");
                lines[i][j].setBackground(Color.CYAN);
                lines[i][j].setBounds(i * height, j * width, width, height);
                this.add(lines[i][j]);
            }
        }
    }

}
