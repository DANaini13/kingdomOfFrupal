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
                case KeyEvent.VK_UP: System.out.println("up"); break;
                case KeyEvent.VK_LEFT: System.out.println("left");break;
                case KeyEvent.VK_DOWN: System.out.println("down");break;
                case KeyEvent.VK_RIGHT: System.out.println("right");break;
            }
        });
    }

}
