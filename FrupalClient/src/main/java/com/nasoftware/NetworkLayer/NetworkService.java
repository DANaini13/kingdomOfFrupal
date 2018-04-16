package com.nasoftware.NetworkLayer;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkService {
    static private NetworkService networkService = null;
    static public NetworkService getNetworkService() {
        if(networkService == null) {
            networkService = new NetworkService();
            networkService.setReceiveService();
        }
        return networkService;
    }

    private HashMap<String, JSONObject> packetBuffer = new HashMap<>();
    private Lock lock = new ReentrantLock();

    public void CGIRequest(JSONObject args, CompletionHandler handler) {
        SocketService socketService = SocketService.getSocketService();
        socketService.SendPacket(args);
        try {
            lock.lock();
            JSONObject response = packetBuffer.get(args.get("command").toString());
            lock.unlock();
            while (response == null) {
                Thread.sleep(100);
                response = packetBuffer.get(args.get("command").toString());
            }
            handler.response(response);
            packetBuffer.put(args.get("command").toString(), null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void PUSHRequest(JSONObject args) {
        SocketService socketService = SocketService.getSocketService();
        socketService.SendPacket(args);
    }

    public void SetSnycRequest(String command, CompletionHandler handler) {
        JSONObject response = packetBuffer.get(command);
        while (true) {
            try {
                Thread.sleep(100);
                response = packetBuffer.get(command);
                if(response != null) {
                    handler.response(response);
                    packetBuffer.put(command, null);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = packetBuffer.get(command);
        }
    }

    private void setReceiveService() {
        SocketService socketService = SocketService.getSocketService();
        socketService.recivePacketService((response) -> {
            try {
                String command = response.get("command").toString();
                lock.lock();
                packetBuffer.put(command, response);
                lock.unlock();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
