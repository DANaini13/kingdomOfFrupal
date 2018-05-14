package com.nasoftware.LogicLayer;

import com.google.common.hash.Hashing;
import com.nasoftware.DataLayer.AccountDataService;
import com.nasoftware.DataLayer.Player;
import com.nasoftware.DataLayer.PlayerDataService;
import com.nasoftware.DataLayer.PlayerManager;
import com.nasoftware.NetworkLayer.ServerManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class AccountService {
    static private AccountService accountService = null;
    static public synchronized AccountService getAccountService() {
        if(accountService == null) {
            accountService = new AccountService();
        }
        return accountService;
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
                    PlayerService.getPlayerService().move(account, "nothing");
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
            PlayerService.getPlayerService().move(account, "nothing");
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
}
