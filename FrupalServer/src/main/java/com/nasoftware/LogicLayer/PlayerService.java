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
                break;
            case "down":
                y += 1;
                break;
            case "left":
                x -= 1;
                break;
            case "right":
                x += 1;
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

        switch (map[x][y].name) {
            case "Boulder": energyChanged -= 20; break;
            case "Tree": energyChanged -= 10; break;
            case "Blackberry": energyChanged -= 6; break;
        }
        map[x][y].visiable = true;
        playerManager.resetEnergy(account, energyChanged);
        playerManager.resetPosition(account, x, y);
        ServerManager serverManager = ServerManager.getServerManager(2022);
        JSONObject jsonObject = generateResponsePacket();
        serverManager.sendNotifications(jsonObject);
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
                player.put("energy", next.energy);
                player.put("wealth", next.wealth);
                player.put("tools", next.toolList);
                userList.add(player);
            }
            result.put("playerList", userList);
            GameItem[][] gameItems = MapService.getMap();
            LinkedList<JSONObject> mapList = new LinkedList<>();
            int mapWidth = MapService.getMapWidth();
            for(int i=0; i<mapWidth; ++i) {
                for(int j=0; j<mapWidth; ++j) {
                    GameItem item = gameItems[i][j];
                    if(item.name.equals("None") && item.type.equals("meadow"))
                        continue;
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
