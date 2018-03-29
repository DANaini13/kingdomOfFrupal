package com.company;

public class Main {
    public static void main(String[] args){
        ScreenView screenView = new ScreenView(20, 20);
        screenView.start();
        screenView.outputToXY("Screen Example!", 2, 10);
        for (int i = 0; i<10; ++i) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            screenView.outputToXY('*', i + 5, 8);
        }
        screenView.truenOffScreen();
   }
}
