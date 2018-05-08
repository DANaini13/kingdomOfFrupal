package com.nasoftware.GameLayer;
import com.nasoftware.LogicLayer.AccountService;
import com.nasoftware.LogicLayer.GameStatusService;
import com.nasoftware.LogicLayer.MovementService;
import com.nasoftware.NetworkLayer.NetworkService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class GameViewController extends JPanel implements KeyListener {
    static private GameViewController gameViewController = null;
    static private LinkedList<Player> tempList;
    static private GameItem[][] original;
    static public GameViewController getGameViewController() {
        if(gameViewController == null) {
            gameViewController = new GameViewController();
            gameViewController.setLayout(null);
            JFrame frame = new JFrame("Kingdom of Frupal");
            frame.setLayout(null);
            frame.setBounds(0, 0, 700, 700);
            frame.setResizable(false);
            frame.setVisible(true);

            GameView gameView = new GameView();
            gameView.setLayout(null);
            gameView.setBounds(180, 160, 500, 500);
            gameView.setBackground(Color.lightGray);
            gameViewController.gameView = gameView;
            frame.add(gameView);

            GameAlertView gameAlertView = GameAlertView.getGameAlertView();
            gameAlertView.setBounds(180, 120, 500, 30);
            gameAlertView.setLayout(null);
            frame.add(gameAlertView);

            gameViewController.setFocusable(true);
            gameViewController.requestFocus();
            gameViewController.addKeyListener(gameViewController);
            frame.add(gameViewController);

            StatusView statusView = new StatusView();
            statusView.setLayout(null);
            statusView.setBounds(180, 0, 500, 120);
            frame.add(statusView);

            ToolsView toolsView = new ToolsView();
            toolsView.setLayout(null);
            toolsView.setBounds(0, 0, 180, 700);
            frame.add(toolsView);

            /**
             * Game Sync Events Below:
             */
            // player status changes event
            GameStatusService gameStatusService = GameStatusService.getGameStatusService();
            gameStatusService.setStatusSyncHandler((response) -> {
                try {
                    JSONArray playList = response.getJSONArray("playerList");
                    for(int i=0; i<playList.length(); ++i) {
                        Player player = new Player();
                        JSONObject jsonObject = playList.getJSONObject(i);
                        player.x = jsonObject.getInt("x");
                        player.y = jsonObject.getInt("y");
                        player.direction = jsonObject.getInt("direction");
                        player.energy = Integer.parseInt(jsonObject.get("energy").toString());
                        player.wealth = Integer.parseInt(jsonObject.get("wealth").toString());
                        player.account = jsonObject.getString("name");
                        JSONArray tools = jsonObject.getJSONArray("tools");
                        for (int j=0; j<tools.length(); ++j) {
                            player.toolList.add(tools.getString(j));
                        }
                        gameViewController.addPlayer(player);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int mapWidth = response.getInt("mapWidth");
                    GameItem gameItem[][] = new GameItem[mapWidth][mapWidth];
                    for (int y = 0; y < mapWidth; ++y) {
                        for (int x = 0; x < mapWidth; ++x) {
                            gameItem[x][y] = new GameItem("meadow", "None");
                        }
                    }
                    JSONArray mapList = response.getJSONArray("map");
                    for(int i=0; i<mapList.length(); ++i) {
                        JSONObject item = mapList.getJSONObject(i);
                        int x = item.getInt("x");
                        int y = item.getInt("y");
                        String type = item.getString("type");
                        String name = item.getString("name");
                        gameItem[x][y] = new GameItem(type, name);
                        if(!item.getString("visibleList").equals("empty")) {
                            JSONArray visibleList = item.getJSONArray("visibleList");
                            for (int j=0; j<visibleList.length(); ++j) {
                                gameItem[x][y].visibleList.add(visibleList.getJSONObject(j).getString("name"));
                            }
                        }
                    }
                    gameViewController.gameItems = gameItem;
                    gameViewController.mapWidth = mapWidth;
                    gameViewController.copyToOriginal(gameItem, mapWidth);
                    gameView.render(gameItem, mapWidth, gameViewController.getPlayers());
                    statusView.render(gameViewController.getPlayers());
                    tempList = gameViewController.getPlayerList();
                    gameViewController.cleanPlayers();
                    LinkedList<String> linkedList = new LinkedList<>();
                    linkedList.add("test");
                    linkedList.add("test1");
                    linkedList.add("test2");
                    linkedList.add("test3");
                    toolsView.render(linkedList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            // new player join in
            gameStatusService.setNewPlayerAlertHandler((response) -> {
                try {
                    gameAlertView.alert("welcome " + response.getString("name") + " come into the room!!!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            // game over
            gameStatusService.setGameOverHandler((response) -> {
               try {
                   String name = null;
                   if(response.getString("hasWinner").equals("true")){
                       name = response.getString("winner");
                   }
                   GameOverPage gameOverPage = GameOverPage.generateGameOverPage(name, gameViewController.getPlayerList());
                   NetworkService.getNetworkService().closeConnection();
                   sleep(2000);
                   System.exit(0);
                   frame.dispose();
               } catch (JSONException e) {
                   e.printStackTrace();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            });

            // someone were kicked out, include myself
            gameStatusService.setPlayerKickedOutHandler((response) -> {
                try {
                    if(response.getString("account").equals(AccountService.myAccount)) {
                        GameOverPage gameOverPage = GameOverPage.generateGameOverPage();
                        frame.dispose();
                        NetworkService.getNetworkService().closeConnection();
                        sleep(2000);
                        System.exit(0);
                    } else {
                        gameAlertView.alert("sorry, " + response.getString("account") + " were dead!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // removed an Obstacle
            gameStatusService.setPlayerHitObstacleHandler((response) -> {
                try {
                    int energyConsumed = response.getInt("energyConsumed");
                    String obstacle = response.getString("obstacle");
                    gameAlertView.alert("you just removed a " + obstacle + ", which took you " + -energyConsumed + " points energy!!!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            // got power bar
            gameStatusService.setPickPowerBarHandler((response) -> {
                try {
                    int energy = response.getInt("gotEnergy");
                    gameAlertView.alert("Congratulations! you got a power bar with " + energy + " points energy!!!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
        return gameViewController;
    }

    private LinkedList<Player> playerList = new LinkedList<>();
    private GameView gameView;
    private GameItem[][] gameItems;
    private int mapWidth;

    void addPlayer(Player player) {
        this.playerList.add(player);
    }

    LinkedList<Player> getPlayerList() {
        return this.playerList;
    }

    void cleanPlayers() {
        playerList = new LinkedList<>();
    }

    LinkedList<Player> getPlayers() {
        return this.playerList;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        MovementService movementService = MovementService.getMovementService();
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                movementService.move("up");
                break;
            case KeyEvent.VK_LEFT:
                movementService.move("left");
                break;
            case KeyEvent.VK_DOWN:
                movementService.move("down");
                break;
            case KeyEvent.VK_RIGHT:
                movementService.move("right");
                break;
            case 80:
                gameView.renderWithPrivilegeCommand(gameItems, mapWidth, playerList);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == 80) {
            copyFromOriginal(gameItems, mapWidth);
            gameView.render(gameItems, mapWidth, tempList);
        }
    }

    public void copyToOriginal(GameItem[][] gameItem, int mapWidth) {
        original = new GameItem[mapWidth][mapWidth];
        for(int i=0; i<mapWidth; ++i) {
            for(int j=0; j<mapWidth; ++j) {
                original[i][j] = new GameItem(gameItem[i][j]);
            }
        }
    }

    public void copyFromOriginal(GameItem[][] gameItem, int mapWidth) {
        for(int i=0; i<mapWidth; ++i) {
            for(int j=0; j<mapWidth; ++j) {
                gameItem[i][j] = new GameItem(original[i][j]);
            }
        }
    }
}
