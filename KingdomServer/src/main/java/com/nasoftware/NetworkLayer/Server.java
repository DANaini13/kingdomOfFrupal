package com.nasoftware.NetworkLayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread {
    private final Socket server;

    static public Server create(Socket server) {
        Server newServer = new Server(server);
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
            //String buffer = in.readUTF();
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            JSONObject jsonObject = new JSONObject();
            JSONObject secondLevel = new JSONObject();
            secondLevel.put("hello", "world");
            jsonObject.put("hello", "world");
            jsonObject.put("second", secondLevel);
            out.writeUTF(jsonObject.toString());
            /*
            while (!buffer.equals("close")) {
                HashMap<String, String> args = parseJSON(buffer);
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> parseJSON(String content) {
        HashMap result = new HashMap<String, String>();
        String args[] = content.split("&");
        for (String x:args) {
            String parts[] = x.split("=");
            result.put(parts[0], parts[1]);
        }
        return result;
    }

    private void dispatchComman(HashMap<String, String> args) {
        String commandStr = args.get("command");
        Character command = commandStr.charAt(0);
        switch (command) {
            case '1' : break;
            default : break;
        }
    }

}
