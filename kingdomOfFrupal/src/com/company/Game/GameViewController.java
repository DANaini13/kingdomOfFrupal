package com.company.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

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
    private int bankAccountBalance;
    private StatusChangeAction energyChangeAction;

    private GameViewController(GameView gameView) {
        this.gameRole = new int[2];
        this.gameView = gameView;
        this.energyLeft = 100;
        this.bankAccountBalance = 0;
        this.gameOver = false;
        this.energyChangeAction = null;
    }

    static public GameViewController createWithGameView(GameView gameView) {
        GameViewController gameViewController = new GameViewController(gameView);
        return gameViewController;
    }

    private StaticGameItem[][] loadMap() {
        int mapWidth = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("mapFile.txt"));
            String line = bufferedReader.readLine();
            mapWidth = Integer.parseInt(line);
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            String pos[] = line.split(",");
            gameRole[0] = Integer.parseInt(pos[0]);
            gameRole[1] = Integer.parseInt(pos[1]);
            line = bufferedReader.readLine();
            energyLeft = Integer.parseInt(line);
            line = bufferedReader.readLine();
            bankAccountBalance = Integer.parseInt(line);
            line = bufferedReader.readLine();
            LinkedList<String> itemList = new LinkedList<>();
            do {
                itemList.add(line);
                line = bufferedReader.readLine();
            }while (!line.equals("#########################"));
            line = bufferedReader.readLine();

            StaticGameItem map[][] = new StaticGameItem[mapWidth][mapWidth];
            for (int i=0; i<mapWidth; ++i) {
                for (int j=0; j<mapWidth; ++j) {
                    map[i][j] = new StaticGameItem("meadow", "");
                }
            }

            while (line != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int visiable = Integer.parseInt(parts[2]);
                int terrianID = Integer.parseInt(parts[3]);
                String name = parts[4];
                String type = null;
                switch (terrianID) {
                    case 0: type = "meadow"; break;
                    case 1: type = "forest"; break;
                    case 2: type = "water"; break;
                    case 3: type = "wall"; break;
                }
                int energyChanged = 0;
                switch (name) {
                    case "Tree":
                        energyChanged = -10;
                        break;
                    case "Boulder":
                        energyChanged = -20;
                        break;
                    case "Blackberry":
                        energyChanged = -6;
                        break;
                    case "None":
                        break;
                }
                map[x][y] = new StaticGameItem(type, name, energyChanged);
                map[x][y].visiable = visiable == 1;
                line = bufferedReader.readLine();
            }
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void start(){
        gameMap = loadMap();
        this.gameView.prepareForNewMap(gameMap[0].length, gameMap.length);
        this.gameView.updateMapWithGameItems(gameMap);

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
       // if(item.visiable)
         //   return;
        this.energyLeft = this.energyLeft + item.energyChange;
        item.visiable = true;
        System.out.println(this.energyLeft);
    }

    public void setEnergyChangeHandler(StatusChangeAction action) {
        this.energyChangeAction = action;
    }
}
