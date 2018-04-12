package com.company;

import com.company.Game.MainViewController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        /*
        MainViewController mainViewController = MainViewController.create();
        mainViewController.start();
        */

        try {
            Socket client = new Socket("localhost", 2022);
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println(in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
