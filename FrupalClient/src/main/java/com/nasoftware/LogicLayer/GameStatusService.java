package com.nasoftware.LogicLayer;

import com.nasoftware.NetworkLayer.CompletionHandler;
import com.nasoftware.NetworkLayer.NetworkService;

public class GameStatusService {
    private static GameStatusService gameStatusService = null;

    public static GameStatusService getGameStatusService() {
        if(gameStatusService == null)
            gameStatusService = new GameStatusService();
        return gameStatusService;
    }

    public void setStatusSyncHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSnycRequest("syncStatus", handler);
    }
}
