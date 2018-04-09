package com.company.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

interface KeyAction{
    void keyAction(KeyEvent event);
}


public class GameView extends Panel implements KeyListener {

    private JLabel lines[][];
    private int gameRole[];
    private KeyAction keyAction;
    private final char gameRoleChar = '@';
    private StaticGameItem gameMap[][] = null;

    public int width;
    public int height;

    static public GameView createWithBound(int x, int y, int w, int h) {
        GameView gameView = new GameView(x, y, w, h);
        gameView.setLayout(null);
        return gameView;
    }

    private GameView(int x, int y, int w, int h) {
        this.setBounds(x, y, w, h);
        this.setBackground(Color.LIGHT_GRAY);
        this.width = 20;
        this.height = 20;
        initSubviews();
        addKeyListener(this);
    }

    public void prepareForNewMap(int w, int h) {
        this.width = w;
        this.height = h;
        this.lines = new JLabel[this.width][this.height];
        float height = this.getBounds().height / this.height;
        float width  = this.getBounds().width / this.width;
        for (int i=0; i<this.width; ++i) {
            for (int j=0; j<this.height; ++j) {
                lines[i][j] = new JLabel("*");
                float y = (float)j * height;
                float x = (float)i * width;
                lines[i][j].setBounds((int)x, (int)y, (int)width, (int)height);
                this.add(lines[i][j]);
            }
        }
    }

    private void initSubviews() {
        this.keyAction = null;
    }

    public void outputToXY(int x, int y, char content) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
            return;
        lines[x][y].setText(String.valueOf(content));
    }

    public void outputToXY(int x, int y, String content) {
        if (x < 0 || x + content.length() >= this.width || y < 0 || y >= this.height)
            return;
        char sequence[] = content.toCharArray();
        for(int i=0; i<content.length(); ++i)
            lines[x + i][y].setText(String.valueOf(sequence[i]));
    }

    public void outputToXYWithColor(int x, int y, String content, Color color) {
       if (x < 0 || x + content.length() >= this.width || y < 0 || y >= this.height)
            return;
        char sequence[] = content.toCharArray();
        for(int i=0; i<content.length(); ++i) {
            lines[x + i][y].setForeground(color);
            lines[x + i][y].setText(String.valueOf(sequence[i]));
        }
    }

    public void setUpGameRoleWithXY(int x, int y) {
        this.gameRole = new int[2];
        this.gameRole[0] = x;
        this.gameRole[1] = y;
        lines[x][y].setText(String.valueOf(this.gameRoleChar));
    }

    public boolean moveGameRoleWithXY(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
            return false;
        this.gameRole[0] = x;
        this.gameRole[1] = y;
        lines[x][y].setText(String.valueOf(this.gameRoleChar));
        return true;
    }

    public void updateMapWithGameItems(StaticGameItem gameItems[][]) {
        int height = gameItems.length;
        if(height != this.height)
            return;
        int width = gameItems[0].length;
        if(width != this.width)
            return;
        this.gameMap = gameItems;
    }

    public void render() {
        if(this.gameMap == null)
            return;
        for(int i=0; i<width; ++i) {
            for(int j=0; j<height; ++j) {
                if(i == this.gameRole[0] && j == this.gameRole[1]){
                    this.lines[i][j].setForeground(Color.BLACK);
                    continue;
                }
                switch (this.gameMap[i][j].type) {
                    case "water": {
                        this.lines[i][j].setForeground(Color.BLUE);
                        break;
                    }
                    case "meadow": {
                        this.lines[i][j].setForeground(Color.DARK_GRAY);
                        break;
                    }
                    case "forest": {
                        this.lines[i][j].setForeground(Color.GREEN);
                        break;
                    }
                    case "wall": {
                        this.lines[i][j].setForeground(Color.YELLOW);
                        break;
                    }
                }
                if(!this.gameMap[i][j].visiable) {
                    this.lines[i][j].setForeground(Color.GRAY);
                }
                switch (this.gameMap[i][j].name) {
                    case "None":
                        this.lines[i][j].setText("*");
                        break;
                    case "Tree":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("木");
                        else
                            this.lines[i][j].setText("*");
                        break;
                    case "Boulder":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("■");
                        else
                            this.lines[i][j].setText("*");
                        break;
                    case "Blackberry":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("B");
                        else
                            this.lines[i][j].setText("*");
                        break;
                    case "Bushes":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("丛");
                        else
                            this.lines[i][j].setText("*");
                        break;
                    case "PowerBar":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("=");
                        else
                            this.lines[i][j].setText("*");
                        break;
                    case "Chest":
                        if(this.gameMap[i][j].visiable)
                            this.lines[i][j].setText("=");
                        else
                            this.lines[i][j].setText("||");
                        break;
                    default:
                        this.lines[i][j].setText("*");
                }
            }
        }
    }

    public void setUpKeyAction(KeyAction keyAction) {
        this.keyAction = keyAction;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.keyAction != null) {
            this.keyAction.keyAction(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
