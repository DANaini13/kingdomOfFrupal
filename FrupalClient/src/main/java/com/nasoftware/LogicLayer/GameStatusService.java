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
        networkService.SetSyncRequest("syncStatus", handler);
    }

    public void setNewPlayerAlertHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("newPlayer", handler);
    }

    public void setGameOverHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("gameOver", handler);
    }

    public void setPlayerKickedOutHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("kickedOut", handler);
    }

    public void setPlayerHitObstacleHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("removeObstacle", handler);
    }

    public void setPickPowerBarHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("pickPowerBar", handler);
    }

    public void setSeverNotifyHandler(CompletionHandler handler) {
        NetworkService networkService = NetworkService.getNetworkService();
        networkService.SetSyncRequest("notify", handler);
    }
}
