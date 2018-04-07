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

    public final int width;
    public final int height;

    static public GameView createWithBound(int x, int y, int w, int h, int mapWidth, int mapHeight) {
        GameView gameView = new GameView(x, y, w, h, mapWidth, mapHeight);
        gameView.setLayout(null);
        return gameView;
    }

    private GameView(int x, int y, int w, int h, int mapWidth, int mapHeight) {
        this.setBounds(x, y, w, h);
        this.width = mapWidth;
        this.height = mapHeight;
        this.setBackground(Color.gray);
        initSubviews();
        addKeyListener(this);
    }

    private void initSubviews() {
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
                if(i == this.gameRole[0] && j == this.gameRole[1])
                    continue;
                switch (this.gameMap[i][j].type) {
                    case "empty":
                        if(!this.gameMap[i][j].visiable) {
                            this.lines[i][j].setText("*");
                            this.lines[i][j].setForeground(Color.BLACK);
                        }
                        else {
                            this.lines[i][j].setText("*");
                            this.lines[i][j].setForeground(Color.CYAN);
                        }
                        break;
                    case "tree":
                        if(this.gameMap[i][j].visiable) {
                            this.lines[i][j].setText("T");
                            this.lines[i][j].setForeground(Color.RED);
                        }
                        break;
                    case "boulder":
                        if(this.gameMap[i][j].visiable) {
                            this.lines[i][j].setText("B");
                            this.lines[i][j].setForeground(Color.RED);
                        }
                        break;
                    default:break;
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
