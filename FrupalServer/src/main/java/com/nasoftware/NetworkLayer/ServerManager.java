package com.nasoftware.NetworkLayer;

import org.json.JSONObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerManager extends Thread {
    private static ServerManager serverManager = null;

    public static ServerManager getServerManager(int port) {
        if(serverManager == null) {
            serverManager = new ServerManager(port);
            serverManager.start();
        }
        return serverManager;
    }

    private final int port;
    private HashMap<String, Server> serverMap;
    private Lock mapLock = new ReentrantLock();
    private Integer serverID = 0;

    ServerManager(int port) {
        this.port = port;
        this.serverMap = new HashMap<>();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket server = serverSocket.accept();
                Server newServer = Server.create(server, serverID);
                this.serverMap.put(serverID.toString(), newServer);
                ++serverID;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNotifications(JSONObject message) {
        mapLock.lock();
        Iterator it = serverMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Server server = (Server)entry.getValue();
            server.sendPack(message.toString());
        }
        mapLock.unlock();
    }

    public void removeServer(Integer serverID) {
        mapLock.lock();
        this.serverMap.remove(serverID.toString());
        mapLock.unlock();
    }
}
