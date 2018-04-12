package com.nasoftware.NetworkLayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerManager extends Thread {
    private final int port;
    private HashMap<String, Server> serverMap;

    public static ServerManager create(int port) {
        ServerManager serverManager = new ServerManager(port);
        serverManager.start();
        return serverManager;
    }

    ServerManager(int port) {
        this.port = port;
        this.serverMap = new HashMap<String, Server>();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket server = serverSocket.accept();
                Server newServer = Server.create(server);
                this.serverMap.put("0", newServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
