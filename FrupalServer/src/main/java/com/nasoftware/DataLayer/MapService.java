package com.nasoftware.DataLayer;

import com.nasoftware.GameItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapService {
    static private GameItem gameItem[][] = null;
    static private Lock lock = new ReentrantLock();
    static private int mapWidth = 0;

    static public GameItem[][] getMap() {
        if(gameItem == null) {
            lock.lock();
            gameItem = loadMap();
            GameItem[][] result = new GameItem[mapWidth][mapWidth];
            for(int y = 0; y < mapWidth; ++y) {
                for(int x = 0; x <mapWidth; ++x) {
                    result[x][y] = new GameItem(gameItem[x][y]);
                }
            }
            lock.unlock();
        }
        return gameItem;
    }

    static public int getMapWidth() {
        if(gameItem == null) {
            lock.lock();
            gameItem = loadMap();
            lock.unlock();
        }
        return mapWidth;
    }

    static public void resetMap() {
        lock.lock();
        gameItem = loadMap();
        lock.unlock();
    }

    static public void removeItem(int x, int y) {
        lock.lock();
        gameItem[x][y].name = "None";
        lock.unlock();
    }

    static public void removePlayer(String account) {
        lock.lock();
        for(int x = 0; x < mapWidth; ++x) {
            for(int y = 0; y < mapWidth; ++y) {
                if(gameItem[x][y].visibleList.contains(account)) {
                    gameItem[x][y].visibleList.remove(account);
                }
            }
        }
        lock.unlock();
    }

    static public void changeVisibility(String account, int x, int y) {
        lock.lock();
        try {
            if (!gameItem[x][y].visibleList.contains(account)) {
                gameItem[x][y].visibleList.add(account);
            }
        }catch (ArrayIndexOutOfBoundsException e) {

        }finally {
            lock.unlock();
        }
    }

    static private GameItem[][] loadMap() {
        GameItem[][] result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("mapFile.txt"));
            String line = bufferedReader.readLine();
            mapWidth = Integer.parseInt(line);
            lock.lock();
            result = new GameItem[mapWidth][mapWidth];
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (!line.equals("#########################")) {
                line = bufferedReader.readLine();
            }
            line = bufferedReader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                int y = Integer.parseInt(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int terrainID = Integer.parseInt(parts[3]);
                String name = parts[4];
                String type = null;
                switch (terrainID) {
                    case 0: type = "meadow"; break;
                    case 1: type = "forest"; break;
                    case 2: type = "water"; break;
                    case 3: type = "wall"; break;
                    case 4: type = "desert"; break;
                    case 5: type = "bog"; break;
                }
                result[x][y] = new GameItem(type, name);
                line = bufferedReader.readLine();
            }
            for(int y = 0; y < mapWidth; ++y) {
                for(int x = 0; x < mapWidth; ++x) {
                    if(result[x][y] == null) {
                        result[x][y] = new GameItem("meadow", "None");
                    }
                }
            }
            for(int y = 0; y < mapWidth; ++y) {
                for(int x = 0; x < mapWidth; ++x) {
                    System.out.print(result[x][y].toString() + " ");
                }
                System.out.println();
            }
            lock.unlock();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    static public boolean checkUsable(String toolName, int x, int y) {
        if(toolName.equals("Binoculars"))
            return true;
        for(int i=x-1; i<=x+1; ++i) {
            int j = y;
                switch (toolName) {
                    case "Jack Hammer": case "Hammer and Chisel":
                        if(gameItem[i][j].name.equals("Boulder"))
                            return true;
                        break;
                    case "Chain Saw": case "Axe":
                        if(gameItem[i][j].name.equals("Tree"))
                            return true;
                        break;
                    case "Shears": case "Pruning Saw":
                        if(gameItem[i][j].name.equals("Blackberry"))
                            return true;
                        break;
                    case "Boat":
                        if(gameItem[i][j].type.equals("water"))
                            return true;
                        break;
                }
        }
        for(int j=y-1; j<=y+1; ++j) {
            int i = x;
            switch (toolName) {
                case "Jack Hammer": case "Hammer and Chisel":
                    if(gameItem[i][j].name.equals("Boulder"))
                        return true;
                    break;
                case "Chain Saw": case "Axe":
                    if(gameItem[i][j].name.equals("Tree"))
                        return true;
                    break;
                case "Shears": case "Pruning Saw":
                    if(gameItem[i][j].name.equals("Blackberry"))
                        return true;
                    break;
                case "Boat":
                    if(gameItem[i][j].type.equals("water"))
                        return true;
                    break;
                }
        }
        return false;
    }
}
