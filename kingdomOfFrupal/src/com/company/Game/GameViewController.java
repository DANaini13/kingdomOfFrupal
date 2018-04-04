package com.company.Game;

import java.awt.event.KeyEvent;

public class GameViewController {

    private GameView gameView;
    /*
    The map that stores all the game runtime information
     */
    private int gameMap[][];

    private GameViewController(GameView gameView) {
        this.gameView = gameView;
    }

    static public GameViewController createWithGameView(GameView gameView) {
        GameViewController gameViewController = new GameViewController(gameView);
        return gameViewController;
    }

    public void start() {
        this.gameView.setUpGameRoleWithXY(5, 5);
        this.gameView.setUpKeyAction(keyEvent -> {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_UP: gameView.moveRoleUp(); break;
                case KeyEvent.VK_LEFT: gameView.moveRoleLeft(); break;
                case KeyEvent.VK_DOWN: gameView.moveRoleDown(); break;
                case KeyEvent.VK_RIGHT: gameView.moveRoleRight(); break;
            }
        });
    }

}
