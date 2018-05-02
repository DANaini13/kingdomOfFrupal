package com.nasoftware.LogicLayer;

import com.nasoftware.DataLayer.MapService;
import com.nasoftware.DataLayer.Player;
import com.nasoftware.DataLayer.PlayerManager;
import com.nasoftware.GameItem;
import com.nasoftware.NetworkLayer.ServerManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayerService {
    static private PlayerService playerService = null;

    static public PlayerService getPlayerService() {
        if(playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    public void login(String account, CompletionHandler handler) {
        PlayerManager playerManager = PlayerManager.getPlayerManager();
        LinkedList<Player> userList = playerManager.getPlayerList();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "login");
            Player temp = new Player();
            temp.account = account;
            if(userList.contains(temp)) {
                jsonObject.put("error", "the account already exist!");
                handler.response(jsonObject);
                return;
            }
            if(userList.size() >= 4) {
                jsonObject.put("error", "the room is full, please check later");
                handler.response(jsonObject);
                return;
            }
            playerManager.addPlayer(account);
            jsonObject.put("error", 0);
            handler.response(jsonObject);
            move(account, "nothing");
            System.out.println("new Person login!");
            System.out.println(playerManager.getPlayerList());
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("command", "newPlayer");
            jsonObject1.put("name", account);
            ServerManager serverManager = ServerManager.getServerManager(2022);
            serverManager.sendNotifications(jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void move(String account, String direction) {

        PlayerManager playerManager = PlayerManager.getPlayerManager();
        LinkedList<Player> players = playerManager.getPlayerList();
        if(players.size() <= 0)
            return;
        Iterator it = players.iterator();
        int x = 0;
        int y = 0;
        while (it.hasNext()) {
            Player player = (Player)it.next();
            if(player.account.equals(account)) {
                x = player.x;
                y = player.y;
            }
        }
        switch (direction) {
            case "up":
                y -= 1;
                playerManager.resetDirection(account, 0);
                break;
            case "down":
                y += 1;
                playerManager.resetDirection(account, 2);
                break;
            case "left":
                x -= 1;
                playerManager.resetDirection(account, 3);
                break;
            case "right":
                x += 1;
                playerManager.resetDirection(account, 1);
                break;
            default:
                break;
        }
        int energyChanged = 0;
        GameItem[][] map = MapService.getMap();
        switch (map[x][y].type) {
            case "wall": return;
            case "meadow": energyChanged -= 1; break;
            case "water": return;
            case "forest": energyChanged -= 1; break;
            case "desert": energyChanged -= 2; break;
        }
        boolean flag = false;
        switch (map[x][y].name) {
            case "Boulder": energyChanged -= 20; break;
            case "Tree": energyChanged -= 10; break;
            case "Blackberry": energyChanged -= 6; break;
            case "Diamond": flag = true; break;
        }
        map[x][y].visiable = true;
        playerManager.resetEnergy(account, energyChanged);
        playerManager.resetPosition(account, x, y);
        ServerManager serverManager = ServerManager.getServerManager(2022);
        JSONObject jsonObject = generateResponsePacket();
        serverManager.sendNotifications(jsonObject);
        if(flag) {
            JSONObject gameoverObject = new JSONObject();
            try {
                gameoverObject.put("command", "gameOver");
                gameoverObject.put("hasWinner", "true");
                gameoverObject.put("winner", account);
                serverManager.sendNotifications(gameoverObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private JSONObject generateResponsePacket() {
        JSONObject result = new JSONObject();
        try {
            result.put("command", "syncStatus");
            List<JSONObject> userList = new LinkedList<>();
            LinkedList<Player> playerList = PlayerManager.getPlayerManager().getPlayerList();
            Iterator it = playerList.iterator();
            while (it.hasNext()) {
                Player next = (Player) it.next();
                JSONObject player = new JSONObject();
                player.put("x", next.x);
                player.put("y", next.y);
                player.put("direction", next.direction);
                player.put("energy", next.energy);
                player.put("wealth", next.wealth);
                player.put("tools", next.toolList);
                player.put("name", next.account);
                userList.add(player);
            }
            result.put("playerList", userList);
            GameItem[][] gameItems = MapService.getMap();
            LinkedList<JSONObject> mapList = new LinkedList<>();
            int mapWidth = MapService.getMapWidth();
            for(int i=0; i<mapWidth; ++i) {
                for(int j=0; j<mapWidth; ++j) {
                    GameItem item = gameItems[i][j];
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("x", i);
                    jsonObject.put("y", j);
                    jsonObject.put("visible", item.visiable? 1:0);
                    jsonObject.put("type", item.type);
                    jsonObject.put("name", item.name);
                    mapList.add(jsonObject);
                }
            }
            result.put("map", mapList);
            result.put("mapWidth", mapWidth);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
