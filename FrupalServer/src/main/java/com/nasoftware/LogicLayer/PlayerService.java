package com.nasoftware.LogicLayer;

import com.google.common.hash.Hashing;
import com.nasoftware.DataLayer.AccountDataService;
import com.nasoftware.DataLayer.MapService;
import com.nasoftware.DataLayer.Player;
import com.nasoftware.DataLayer.PlayerManager;
import com.nasoftware.GameItem;
import com.nasoftware.NetworkLayer.ServerManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
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

    public void signUp(String account, String password, CompletionHandler handler) {
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        AccountDataService accountDataService = AccountDataService.getAccountDataService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "signUp");
            if(accountDataService.addAccount(account, password)) {
                jsonObject.put("error", 0);
            }else {
                jsonObject.put("error","Sorry, account already exist!");
            }
            handler.response(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void login(String account, String password, CompletionHandler handler) {
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        AccountDataService accountDataService = AccountDataService.getAccountDataService();
        int result = accountDataService.varifyAccount(account, password);
        JSONObject jsonObject = new JSONObject();
        try {
            if(result == -1) {
                jsonObject.put("command", "login");
                jsonObject.put("error", "sorry, cannot find your account!");
                handler.response(jsonObject);
                return;
            } else if(result == -2){
                jsonObject.put("command", "login");
                jsonObject.put("error", "sorry, your password is incorrect!");
                handler.response(jsonObject);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PlayerManager playerManager = PlayerManager.getPlayerManager();
        LinkedList<Player> userList = playerManager.getPlayerList();
        try {
            jsonObject.put("command", "login");
            Player temp = new Player();
            temp.account = account;
            if(userList.contains(temp)) {
                jsonObject.put("error", 0);
                handler.response(jsonObject);
                move(account, "nothing");
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
        int tempX = x;
        int tempY = y;
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
        ServerManager serverManager = ServerManager.getServerManager(2022);
        int energyChanged = 0;
        GameItem[][] map = MapService.getMap();
        switch (map[x][y].type) {
            case "wall": x = tempX; y = tempY; energyChanged -= 1; break;
            case "meadow": energyChanged -= 1; break;
            case "water": x = tempX; y = tempY; energyChanged -= 1; break;
            case "forest": energyChanged -= 1; break;
            case "desert": energyChanged -= 2; break;
        }
        boolean flag = false;
        boolean hitObstacle = false;
        int energyConsumed = 0;
        switch (map[x][y].name) {
            case "Boulder": energyConsumed = -20; hitObstacle = true; break;
            case "Tree": energyConsumed = -10; hitObstacle = true; break;
            case "Blackberry": energyConsumed = -6; hitObstacle = true; break;
            case "Diamond": flag = true; hitObstacle = true; break;
            case "PowerBar":
                energyChanged += 12;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", "pickPowerBar");
                    jsonObject.put("gotEnergy", 12);
                    serverManager.pushMessageTo(account, jsonObject);
                    MapService.removeItem(x, y);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        energyChanged += energyConsumed;

        if(hitObstacle) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "removeObstacle");
                jsonObject.put("obstacle", map[x][y].name);
                jsonObject.put("energyConsumed", energyConsumed);
                serverManager.pushMessageTo(account, jsonObject);
                MapService.removeItem(x, y);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!map[x][y].visibleList.contains(account)) {
            map[x][y].visibleList.add(account);
        }
        playerManager.resetEnergy(account, energyChanged);
        playerManager.resetPosition(account, x, y);

        it = players.iterator();
        while (it.hasNext()) {
            Player player = (Player)it.next();
            if(player.account.equals(account) && player.energy < 0) {
                JSONObject kickedOutMessage = new JSONObject();
                try {
                    kickedOutMessage.put("command", "kickedOut");
                    kickedOutMessage.put("account", player.account);
                    serverManager.sendNotifications(kickedOutMessage);
                    playerManager.removePlayer(account);
                    MapService.removePlayer(account);
                    if(playerManager.getPlayerList().size() <= 0) {
                        MapService.resetMap();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        JSONObject jsonObject = generateResponsePacket();
        serverManager.sendNotifications(jsonObject);
        if(flag) {
            JSONObject gameOverObject = new JSONObject();
            try {
                gameOverObject.put("command", "gameOver");
                gameOverObject.put("hasWinner", "true");
                gameOverObject.put("winner", account);
                serverManager.sendNotifications(gameOverObject);
                MapService.resetMap();
                playerManager.removeAll();
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
                    if(item.visibleList.size() <= 0) {
                        jsonObject.put("visibleList", "empty");
                    }else {
                        LinkedList<JSONObject> visibleList = new LinkedList<>();
                        Iterator playerIt = item.visibleList.iterator();
                        while (playerIt.hasNext()) {
                            JSONObject temp = new JSONObject();
                            temp.put("name", playerIt.next().toString());
                            visibleList.add(temp);
                        }
                        jsonObject.put("visibleList", visibleList);
                    }
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
