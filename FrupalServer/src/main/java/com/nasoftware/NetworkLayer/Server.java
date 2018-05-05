package com.nasoftware.NetworkLayer;
import com.nasoftware.DataLayer.PlayerManager;
import com.nasoftware.LogicLayer.PlayerService;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {
    private final Socket server;
    private Lock lock = new ReentrantLock();
    private String account = "";
    private Integer serverID;

    static public Server create(Socket server, int id) {
        Server newServer = new Server(server);
        newServer.serverID = id;
        newServer.start();
        return newServer;
    }

    public Server(Socket server) {
        this.server = server;
    }

    public void run() {
        JSONObject json = new JSONObject();
        try {
            DataInputStream in = new DataInputStream(server.getInputStream());
            String buffer = in.readUTF();
            JSONObject args = new JSONObject(buffer);
            while (!buffer.equals("close")) {
                dispatchCommand(new JSONObject(buffer));
                buffer = in.readUTF();
            }
            ServerManager.getServerManager(2202).removeServer(serverID);
        } catch (IOException e) {
            PlayerManager playerManager = PlayerManager.getPlayerManager();
            playerManager.removePlayer(account);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatchCommand(JSONObject args) {
        try {
            String command = args.get("command").toString();
            switch (command) {
                case "login": {
                    PlayerService playerService = PlayerService.getPlayerService();
                    playerService.login(args.get("account").toString(), (JSONObject response) -> {
                        try {
                            if (response.get("error").toString().equals("0")) {
                                this.account = args.get("account").toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sendPack(response.toString());
                    });
                    break;
                }
                case "move": {
                    PlayerService playerService = PlayerService.getPlayerService();
                    playerService.move(this.account, args.get("operation").toString());
                    break;
                }
                default:
                    System.out.println(args.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendPack(String pack) {
        try {
            lock.lock();
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF(pack);
            lock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
