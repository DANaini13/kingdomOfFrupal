package com.nasoftware.LogicLayer;

import com.nasoftware.NetworkLayer.NetworkService;
import org.json.JSONException;
import org.json.JSONObject;

public class MovementService {
    static private MovementService movementService = null;
    static public MovementService getMovementService() {
        if(movementService == null) {
            movementService = new MovementService();
        }
        return movementService;
    }

    public void move(String direction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "move");
            jsonObject.put("operation", direction);
            NetworkService networkService = NetworkService.getNetworkService();
            networkService.PUSHRequest(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void userTool(String toolName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "useItem");
            jsonObject.put("itemName", toolName);
            NetworkService networkService = NetworkService.getNetworkService();
            networkService.PUSHRequest(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
