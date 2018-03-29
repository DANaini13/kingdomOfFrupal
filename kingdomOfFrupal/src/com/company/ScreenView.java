package com.company;

import java.util.concurrent.locks.ReentrantLock;

public class ScreenView extends Thread {
    private char[][] screenContent;
    private ReentrantLock lock = new ReentrantLock();
    private ReentrantLock switchLock = new ReentrantLock();
    private boolean on = true;

    public final int width;
    public final int height;

    public ScreenView(int width, int height) {
        this.width = width;
        this.height = height;
        screenContent = new char[width][height];
    }

    @Override
    public void run() {
        super.run();
        while(on) {
            try {
                sleep(100);
                render();
            } catch (InterruptedException e) {
                System.err.println("unknow error!!!");
            }
        }
    }

    public void truenOffScreen() {
        switchLock.lock();
        this.on = false;
        switchLock.unlock();
    }

    public void cleanTheScreen() {
        lock.lock();
        for (int i=0; i<width; ++i) {
            for (int j=0; j<height; ++j) {
                screenContent[i][j] = ' ';
            }
        }
        lock.unlock();
    }

    public boolean outputToXY(char content, int x, int y) {
        if(x >= width || x < 0 || y >= height || y < 0)
            return false;
        lock.lock();
        screenContent[x][y] = content;
        lock.unlock();
        return true;
    }

    public boolean outputToXY(String content, int x, int y) {
        if(x + content.length() >= width || x < 0 || y >= height || y < 0)
            return false;
        char[] characters = content.toCharArray();
        lock.lock();
        for(int i = 0; i<content.length(); ++i)
            screenContent[x + i][y] = characters[i];
        lock.unlock();
        return true;
    }

    private void render(){
        StringBuffer buffer = new StringBuffer("");
        for(int i=0; i<height; ++i) {
            for(int j=0; j<width; ++j) {
                if(screenContent[j][i] == ' ')
                    buffer.append(' ');
                else
                    buffer.append(screenContent[j][i]);
                buffer.append(' ');
            }
            buffer.append("\n");
        }
        System.out.println(buffer.toString());
    }
}
