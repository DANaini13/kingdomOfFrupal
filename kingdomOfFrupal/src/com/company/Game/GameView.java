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
    public final int width;
    public final int height;
    private int gameRole[];
    private KeyAction keyAction;

    private final char gameRoleChar = '@';

    static public GameView createWithBound(int x, int y, int w, int h) {
        GameView gameView = new GameView(x, y, w, h);
        gameView.setLayout(null);
        return gameView;
    }

    private GameView(int x, int y, int w, int h) {
        this.setBounds(x, y, w, h);
        this.width = 20;
        this.height = 20;
        this.setBackground(Color.gray);
        initSubviews();
        addKeyListener(this);
    }

    private void initSubviews() {
        this.lines = new JLabel[this.width][this.height];
        int height = this.getBounds().height / (this.height + 1);
        int width  = this.getBounds().width / this.width;
        for (int i=0; i<this.width; ++i) {
            for (int j=0; j<this.height; ++j) {
                lines[i][j] = new JLabel(" ");
                lines[i][j].setBackground(Color.CYAN);
                float y = ((float)j + (float)j/(float)this.height) * (float)height;
                lines[i][j].setBounds(i * width, (int)y, width, height);
                this.add(lines[i][j]);
            }
        }
        this.keyAction = null;
    }

    private void outputToXY(int x, int y, char content) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
            return;
        lines[x][y].setText(String.valueOf(content));
    }

    private void outputToXY(int x, int y, String content) {
        if (x < 0 || x + content.length() >= this.width || y < 0 || y >= this.height)
            return;
        char sequence[] = content.toCharArray();
        for(int i=0; i<content.length(); ++i)
            lines[x + i][y].setText(String.valueOf(sequence[i]));
    }

    public void setUpGameRoleWithXY(int x, int y) {
        this.gameRole = new int[2];
        this.gameRole[0] = x;
        this.gameRole[1] = y;
        lines[x][y].setText(String.valueOf(this.gameRoleChar));
    }

    public void moveGameRoleWithXY(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
            return;
        lines[this.gameRole[0]][this.gameRole[1]].setText(" ");
        this.gameRole[0] = x;
        this.gameRole[1] = y;
        lines[x][y].setText(String.valueOf(this.gameRoleChar));
    }

    public void moveRoleUp() {
        this.moveGameRoleWithXY(this.gameRole[0], this.gameRole[1] - 1);
    }

    public void moveRoleLeft() {
        this.moveGameRoleWithXY(this.gameRole[0] - 1, this.gameRole[1]);
    }

    public void moveRoleDown() {
        this.moveGameRoleWithXY(this.gameRole[0], this.gameRole[1] + 1);
    }

    public void moveRoleRight() {
        this.moveGameRoleWithXY(this.gameRole[0] + 1, this.gameRole[1]);
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
