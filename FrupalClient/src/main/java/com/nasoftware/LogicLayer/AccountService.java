package com.nasoftware.LogicLayer;

import com.nasoftware.NetworkLayer.CompletionHandler;
import com.nasoftware.NetworkLayer.NetworkService;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountService {
    static private AccountService accountService = null;
    static public String myAccount;
    static public AccountService getAccountService() {
        if(accountService == null)
            accountService = new AccountService();
        return accountService;
    }

    public void login(String account, CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "login");
            jsonObject.put("account", account);
            networkService.CGIRequest(jsonObject, handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
