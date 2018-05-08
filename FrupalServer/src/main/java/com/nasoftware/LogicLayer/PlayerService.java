package com.nasoftware.LogicLayer;

import com.google.common.hash.Hashing;
import com.nasoftware.DataLayer.*;
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

    static public synchronized PlayerService getPlayerService() {
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
                PlayerDataService playerDataService = PlayerDataService.getPlayerDataService();
                playerDataService.setPlayerWealth(account, "1000");
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
            for(Player x:userList) {
                if(x.account.equals(account) && !x.online) {
                    jsonObject.put("error", 0);
                    handler.response(jsonObject);
                    move(account, "nothing");
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("command", "notify");
                    jsonObject1.put("content", account + " is now online.");
                    ServerManager.getServerManager(2022).sendNotifications(jsonObject1);
                    playerManager.setPlayerOnlineStatus(true, account);
                    return;
                }else if(x.account.equals(account) && x.online) {
                    jsonObject.put("error", "you already loged in!!!");
                    handler.response(jsonObject);
                    return;
                }

            }
            if(userList.size() >= 4) {
                jsonObject.put("error", "the room is full, please check later");
                handler.response(jsonObject);
                return;
            }
            playerManager.addPlayer(account, PlayerDataService.getPlayerDataService().getPlayerWealth(account));
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
        int wealth = 0;
        while (it.hasNext()) {
            Player player = (Player)it.next();
            if(player.account.equals(account)) {
                x = player.x;
                y = player.y;
                wealth = player.wealth;
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
            case "water":
                if(!playerManager.checkIfUserHasItem(account, "Boat")) {
                    x = tempX; y = tempY; energyChanged -= 1;
                    break;
                }else {
                    break;
                }
            case "forest": energyChanged -= 1; break;
            case "desert": energyChanged -= 2; break;
        }
        boolean flag = false;
        boolean hitObstacle = false;
        int payValue = 0;
        int energyConsumed = 0;
        switch (map[x][y].name) {
            case "Boulder":
                if(playerManager.checkIfUserHasItem(account, "Jack Hammer")) {
                    energyConsumed = -2;
                }else if(playerManager.checkIfUserHasItem(account, "Hammer and Chisel")) {
                    energyConsumed = -10;
                }else {
                    energyConsumed = -20;
                }
                hitObstacle = true;
                break;
            case "Tree":
                if(playerManager.checkIfUserHasItem(account, "Chain Saw")) {
                    energyConsumed = -1;
                }else if(playerManager.checkIfUserHasItem(account, "Axe")) {
                    energyConsumed = -5;
                }else{
                    energyConsumed = -10;
                }
                hitObstacle = true;
                break;
            case "Blackberry":
                if(playerManager.checkIfUserHasItem(account, "Shears")) {
                    energyConsumed = -3;
                }else if(playerManager.checkIfUserHasItem(account, "Pruning Saw")) {
                    energyConsumed = -6;
                }else {
                    energyConsumed = -6;
                }
                hitObstacle = true;
                break;
            case "Diamond": flag = true; hitObstacle = true; break;
            case "Chest1":
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", "notify");
                    jsonObject.put("content", "Congratulations! you wealth increased 1000000");
                    serverManager.pushMessageTo(account, jsonObject);
                    playerManager.increasePlayerWealth(1000000, account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MapService.removeItem(x, y);
                break;
            case "Chest2":
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", "notify");
                    jsonObject.put("content", "Sorry, your wealth was reset to 0!");
                    serverManager.pushMessageTo(account, jsonObject);
                    playerManager.resetPlayerWealth(0, account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MapService.removeItem(x, y);
                break;
            case "PowerBar":
                if(tryPay(wealth, map[x][y].name, account, 10000)) {
                    energyChanged += 12;
                    MapService.removeItem(x, y);
                }
                break;
            case "Binoculars": payValue = 300000; break;
            case "Boat": payValue = 1000000; break;
            case "Rock": payValue = 10000; break;
            case "Jack Hammer": payValue = 400000; break;
            case "Hammer and Chisel": payValue = 50000; break;
            case "Chain Saw": payValue = 300000; break;
            case "Axe": payValue = 150000; break;
            case "Shears": payValue = 100000; break;
            case "Pruning Saw": payValue = 50000; break;
        }

        if(payValue > 0) {
            if(tryPay(wealth, map[x][y].name, account, payValue)) {
                playerManager.addPlayerTools(map[x][y].name, account);
                MapService.removeItem(x, y);
            }
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

    private boolean tryPay(int wealth, String name, String account, int amount) {
        try {
            ServerManager serverManager = ServerManager.getServerManager(2022);
            PlayerManager playerManager = PlayerManager.getPlayerManager();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "notify");
            if(wealth >= amount) {
                jsonObject.put("content", "Congratulations! you got a " + name + "!");
                serverManager.pushMessageTo(account, jsonObject);
                wealth -= amount;
                playerManager.resetPlayerWealth(wealth, account);
                return true;
            }else {
                jsonObject.put("content", "What a pity! you don't have enough whiffles.");
                serverManager.pushMessageTo(account, jsonObject);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
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
