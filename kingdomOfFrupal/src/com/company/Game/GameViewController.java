package com.company.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

interface StatusChangeAction {
    void action(int newEnergy, int newWealth, int x, int y);
}

public class GameViewController {

    private GameView gameView;
    /*
    The map that stores all the game runtime information
     */
    private StaticGameItem gameMap[][];
    private int gameRole[];
    private int energyLeft;
    private boolean gameOver;
    private StatusChangeAction energyChangeAction;

    private GameViewController(GameView gameView) {
        this.gameRole = new int[2];
        this.gameView = gameView;
        this.energyLeft = 100;
        this.gameOver = false;
        this.energyChangeAction = null;
    }

    static public GameViewController createWithGameView(GameView gameView) {
        GameViewController gameViewController = new GameViewController(gameView);
        return gameViewController;
    }

    public void start() {
        gameMap = new StaticGameItem[gameView.width][gameView.height];
        for(int i=0; i<this.gameView.width; ++i)
            for(int j=0; j<this.gameView.height; ++j)
                gameMap[i][j] = new StaticGameItem("empty", "nothing");

        gameMap[5][5] = new StaticGameItem("tree", "Tree", -10);
        gameMap[1][0] = new StaticGameItem("boulder", "Boulder", -20);
        this.gameView.updateMapWithGameItems(gameMap);

        this.gameRole[0] = 0;
        this.gameRole[1] = 0;
        this.gameView.setUpGameRoleWithXY(this.gameRole[0], this.gameRole[1]);
        this.gameMap[this.gameRole[0]][this.gameRole[1]].visiable = true;

        // key actions
        this.gameView.setUpKeyAction(keyEvent -> {
            if(this.gameOver)
                return;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if(this.gameView.moveGameRoleWithXY(this.gameRole[0], this.gameRole[1] - 1))
                        this.gameRole[1] = this.gameRole[1] - 1;
                    break;
                case KeyEvent.VK_LEFT:
                    if(this.gameView.moveGameRoleWithXY(this.gameRole[0] - 1, this.gameRole[1]))
                        this.gameRole[0] = this.gameRole[0] - 1;
                    break;
                case KeyEvent.VK_DOWN:
                    if(this.gameView.moveGameRoleWithXY(this.gameRole[0], this.gameRole[1] + 1))
                        this.gameRole[1] = this.gameRole[1] + 1;
                    break;
                case KeyEvent.VK_RIGHT:
                    if(this.gameView.moveGameRoleWithXY(this.gameRole[0] + 1, this.gameRole[1]))
                        this.gameRole[0] = this.gameRole[0] + 1;
                    break;
            }
            this.energyLeft -= 1;
            performItemEffect(gameMap[this.gameRole[0]][this.gameRole[1]]);
            gameView.render();
            if(this.energyChangeAction != null)
                this.energyChangeAction.action(this.energyLeft, 100, this.gameRole[0], this.gameRole[1]);
            if(this.energyLeft <= 0) {
                gameView.outputToXYWithColor(5, 10, "GAME OVER!", Color.RED);
                this.gameOver = true;
            }
        });
    }

    private void performItemEffect(StaticGameItem item) {
        if(item.visiable)
            return;
        this.energyLeft = this.energyLeft + item.energyChange;
        item.visiable = true;
        System.out.println(this.energyLeft);
    }

    public void setEnergyChangeHandler(StatusChangeAction action) {
        this.energyChangeAction = action;
    }
}
