package com.nasoftware.GameLayer;
import com.nasoftware.LogicLayer.GameStatusService;
import com.nasoftware.LogicLayer.MovementService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class GameViewController extends JPanel implements KeyListener {
    static private GameViewController gameViewController = null;
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
            frame.add(gameView);

            gameViewController.setFocusable(true);
            gameViewController.requestFocus();
            gameViewController.addKeyListener(gameViewController);
            frame.add(gameViewController);

            StatusView statusView = new StatusView();
            statusView.setLayout(null);
            statusView.setBounds(180, 0, 500, 160);
            frame.add(statusView);

            GameStatusService gameStatusService = GameStatusService.getGameStatusService();
            gameStatusService.setStatusSyncHandler((response) -> {
                try {
                    JSONArray playList = response.getJSONArray("playerList");
                    for(int i=0; i<playList.length(); ++i) {
                        Player player = new Player();
                        JSONObject jsonObject = playList.getJSONObject(i);
                        player.x = jsonObject.getInt("x");
                        player.y = jsonObject.getInt("y");
                        player.energy = Integer.parseInt(jsonObject.get("energy").toString());
                        player.wealth = Integer.parseInt(jsonObject.get("wealth").toString());
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
                        gameItem[x][y].visiable = item.getInt("visible") == 1;
                    }
                    gameView.render(gameItem, mapWidth, gameViewController.getPlayers());
                    statusView.render(gameViewController.getPlayers().getFirst());
                    gameViewController.cleanPlayers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
        return gameViewController;
    }

    private LinkedList<Player> playerList = new LinkedList<>();

    void addPlayer(Player player) {
        this.playerList.add(player);
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
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}
}
