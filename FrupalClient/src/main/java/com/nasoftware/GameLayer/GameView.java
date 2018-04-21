package com.nasoftware.GameLayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class GameView extends JPanel {
    void render(GameItem[][] map, int mapWidth, LinkedList<Player> playerList) {
        this.removeAll();
        int width = 500/mapWidth;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player player = (Player)it.next();
            map[player.x - 1][player.y - 1].visiable = true;
            map[player.x][player.y - 1].visiable = true;
            map[player.x + 1][player.y - 1].visiable = true;
            map[player.x - 1][player.y + 1].visiable = true;
            map[player.x][player.y + 1].visiable = true;
            map[player.x + 1][player.y + 1].visiable = true;
            map[player.x - 1][player.y].visiable = true;
            map[player.x + 1][player.y].visiable = true;
        }

        for(int y = 0; y<mapWidth; ++y) {
            for(int x = 0; x<mapWidth; ++x) {
                Iterator it1 = playerList.iterator();
                boolean flag = false;
                while (it1.hasNext()) {
                    Player player = (Player)it1.next();
                    map[player.x - 1][player.y - 1].visiable = true;
                    map[player.x][player.y - 1].visiable = true;
                    map[player.x + 1][player.y - 1].visiable = true;
                    map[player.x - 1][player.y + 1].visiable = true;
                    map[player.x][player.y + 1].visiable = true;
                    map[player.x + 1][player.y + 1].visiable = true;
                    map[player.x - 1][player.y].visiable = true;
                    map[player.x + 1][player.y].visiable = true;
                    if(x == player.x && y == player.y)
                        flag = true;
                }
                if(flag)
                    continue;
                if(!map[x][y].visiable)
                    continue;
                try {
                    BufferedImage image = null;
                    switch (map[x][y].type) {
                        case "water": image = ImageIO.read(new File("Resources/water.png")); break;
                        case "desert": image = ImageIO.read(new File("Resources/desert.png")); break;
                        case "wall": image = ImageIO.read(new File("Resources/wall.png")); break;
                        case "forest": image = ImageIO.read(new File("Resources/forest.png")); break;
                        case "meadow":
                            if(map[x][y].name.equals("None"))
                                continue;
                            break;
                        default:System.out.println("shit");
                    }
                    switch (map[x][y].name) {
                        case "Boulder": image = ImageIO.read(new File("Resources/boulder.png")); break;
                        case "Tree": image = ImageIO.read(new File("Resources/tree.png")); break;
                        case "Blackberry": image = ImageIO.read(new File("Resources/blackberry.png")); break;
                    }
                    JLabel picLabel = new JLabel(new ImageIcon(image));
                    picLabel.setBounds(x*width, y*width, width, width);
                    this.add(picLabel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Iterator it2 = playerList.iterator();
            int i=1;
            while (it2.hasNext()) {
                Player player = (Player)it2.next();
                map[player.x - 1][player.y - 1].visiable = true;
                map[player.x][player.y - 1].visiable = true;
                map[player.x + 1][player.y - 1].visiable = true;
                map[player.x - 1][player.y + 1].visiable = true;
                map[player.x][player.y + 1].visiable = true;
                map[player.x + 1][player.y + 1].visiable = true;
                map[player.x - 1][player.y].visiable = true;
                map[player.x + 1][player.y].visiable = true;
                int x = player.x;
                int y = player.y;
                BufferedImage image = ImageIO.read(new File("Resources/people" + i + ".png"));
                JLabel picLabel = new JLabel(new ImageIcon(image));
                picLabel.setBounds(x*width, y*width, width, width);
                this.add(picLabel);
                ++i;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.repaint();
    }
}
