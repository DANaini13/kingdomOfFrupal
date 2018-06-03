package com.nasoftware.GameLayer;

import com.nasoftware.LogicLayer.AccountService;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class GameView extends JPanel {

    private void setVisiable(GameItem[][] map, int x, int y) {
        try {
            map[x][y].visiable = true;
        }catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    void render(GameItem[][] map, int mapWidth, LinkedList<Player> playerList) {
        this.removeAll();
        int width = 500/mapWidth;
        for(int y = 0; y<mapWidth; ++y) {
            for(int x = 0; x<mapWidth; ++x) {
                Iterator it1 = playerList.iterator();
                boolean flag = false;
                while (it1.hasNext()) {
                    Player player = (Player)it1.next();
                    if(x == player.x && y == player.y)
                        flag = true;
                }
                if(flag)
                    continue;
                if(map[x][y].visibleList.contains(AccountService.myAccount))
                    map[x][y].visiable = true;
                if(!map[x][y].visiable)
                    continue;
                try {
                    BufferedImage image;
                    if(map[x][y].name.equals("None")) {
                        image = getImageWithType(map[x][y].type);
                    }else {
                        image = getImageWithType(map[x][y].name);
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
                String dir = "";
                switch (player.direction) {
                    case 0: dir = "Up"; break;
                    case 1: dir = "Right"; break;
                    case 2: dir = "Down"; break;
                    case 3: dir = "Left"; break;
                }
                String imgName = "Resources/players/player" + i + dir + ".png";
                BufferedImage image = ImageIO.read(new File(imgName));
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

    void renderWithPrivilegeCommand(GameItem[][] map, int mapWidth, LinkedList<Player> playerList) {
        this.removeAll();
        int width = 500/mapWidth;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player player = (Player) it.next();
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
                    if(x == player.x && y == player.y)
                        flag = true;
                }
                if(flag)
                    continue;
                if(map[x][y].visibleList.size() != 0)
                    map[x][y].visiable = true;
                if(!map[x][y].visiable)
                    continue;
                try {
                    BufferedImage image;
                    if(map[x][y].name.equals("None")) {
                        image = getImageWithType(map[x][y].type);
                    }else{
                        image = getImageWithType(map[x][y].name);
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
                String dir = "";
                switch (player.direction) {
                    case 0: dir = "Up"; break;
                    case 1: dir = "Right"; break;
                    case 2: dir = "Down"; break;
                    case 3: dir = "Left"; break;
                }
                String imgName = "Resources/players/player" + i + dir + ".png";
                BufferedImage image = ImageIO.read(new File(imgName));
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

    private BufferedImage getImageWithType(String name) throws IOException {
        BufferedImage image = null;
        switch (name) {
            case "water": image = ImageIO.read(new File("Resources/water.png")); break;
            case "desert": image = ImageIO.read(new File("Resources/desert.png")); break;
            case "wall": image = ImageIO.read(new File("Resources/wall.png")); break;
            case "forest": image = ImageIO.read(new File("Resources/forest.png")); break;
            case "meadow": image = ImageIO.read(new File("Resources/grass.png")); break;
            case "Boulder": image = ImageIO.read(new File("Resources/boulder.png")); break;
            case "Tree": image = ImageIO.read(new File("Resources/tallTree.png")); break;
            case "Blackberry": image = ImageIO.read(new File("Resources/tree.png")); break;
            case "Diamond": image = ImageIO.read(new File("Resources/diamond.png")); break;
            case "PowerBar": image = ImageIO.read(new File("Resources/powerBar.png")); break;
            case "Chest1": image = ImageIO.read(new File("Resources/chest1.png")); break;
            case "Chest2": image = ImageIO.read(new File("Resources/chest2.png")); break;
            case "Binoculars": image = ImageIO.read(new File("Resources/binoculars.png")); break;
            case "Boat": image = ImageIO.read(new File("Resources/boat.png")); break;
            case "Rock": image = ImageIO.read(new File("Resources/rock.png")); break;
            case "Jack Hammer": image = ImageIO.read(new File("Resources/jackHammer.png")); break;
            case "Hammer and Chisel": image = ImageIO.read(new File("Resources/hammerAndChisel.png")); break;
            case "Chain Saw": image = ImageIO.read(new File("Resources/chainSaw.png")); break;
            case "Axe": image = ImageIO.read(new File("Resources/axe.png")); break;
            case "Shears": image = ImageIO.read(new File("Resources/shears.png")); break;
            case "Pruning Saw": image = ImageIO.read(new File("Resources/pruningSaw.png")); break;
            case "TClue": case "FClue": image = ImageIO.read(new File("Resources/clue.png")); break;
            case "bog": image = ImageIO.read(new File("Resources/bog.png")); break;
            default:break;
        }
        return image;
    }
}
