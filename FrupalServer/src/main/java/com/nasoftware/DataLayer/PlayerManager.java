package com.nasoftware.DataLayer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerManager {
    static private PlayerManager playerManager = null;

    static public PlayerManager getPlayerManager() {
        if(playerManager == null)
            playerManager = new PlayerManager();
        return playerManager;
    }

    private LinkedList<Player> playerList = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    public void addPlayer(String account) {
        lock.lock();
        Player player = new Player();
        player.x = 1;
        player.y = 1;
        player.account = account;
        playerList.add(player);
        lock.unlock();
    }

    public LinkedList<Player> getPlayerList() {
        LinkedList<Player> result = new LinkedList<>();
        lock.lock();
        result.addAll(playerList);
        lock.unlock();
        return result;
    }

    public void removePlayer(String account) {
        lock.lock();
        Player player = new Player();
        player.account = account;
        playerList.remove(player);
        lock.unlock();
    }

    public void resetPosition(String account, int x, int y) {
        lock.lock();
        Player player = new Player();
        player.account = account;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player next = (Player) it.next();
            if(next.equals(player)) {
                next.x = x;
                next.y = y;
                break;
            }
        }
        lock.unlock();
    }

    public void resetEnergy(String account, int energyChanged) {
        lock.lock();
        Player player = new Player();
        player.account = account;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player next = (Player) it.next();
            if(next.equals(player)) {
                next.energy += energyChanged;
                break;
            }
        }
        lock.unlock();
    }
}
