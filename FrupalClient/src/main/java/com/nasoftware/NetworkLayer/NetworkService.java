package com.nasoftware.NetworkLayer;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SyncChecker extends Thread
{
    private HashMap<String, JSONObject> source;
    private String command;
    private CompletionHandler handler;
    private Lock lock;

    SyncChecker(HashMap<String, JSONObject> source, String command, CompletionHandler handler, Lock lock) {
        this.source = source;
        this.command = command;
        this.handler = handler;
        this.lock = lock;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                lock.lock();
                JSONObject response = source.get(command);
                if(response != null) {
                    handler.response(response);
                    source.put(command, null);
                    System.out.println("take out packet" + response);
                }
                lock.unlock();
            } catch (InterruptedException e) {
                lock.unlock();
                e.printStackTrace();
            }
        }
    }
}

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
    private HashMap<String, Integer> syncRequestBuffer = new HashMap<>();
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
            System.out.println("take out packet" + response);
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

    public void SetSyncRequest(String command, CompletionHandler handler) {
        if(syncRequestBuffer.containsKey(command))
            return;
        SyncChecker syncChecker = new SyncChecker(packetBuffer, command, handler, lock);
        syncChecker.start();
        syncRequestBuffer.put(command, 1);
    }

    private void setReceiveService() {
        SocketService socketService = SocketService.getSocketService();
        socketService.receivePacketService((response) -> {
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
