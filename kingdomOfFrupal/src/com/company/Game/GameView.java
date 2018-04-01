package com.company.Game;
import java.awt.*;

public class GameView extends Panel {

    static public GameView createWithBound(int x, int y, int w, int h) {
        GameView gameView = new GameView();
        gameView.setLayout(null);
        gameView.setBounds(200, 100, 500, 500);
        return gameView;
    }

    public GameView() {
        initSubviews();
    }

    private void initSubviews() {

    }
}
