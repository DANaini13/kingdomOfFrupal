package com.nasoftware.DataLayer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerManager {
    static private PlayerManager playerManager = null;

    static public synchronized PlayerManager getPlayerManager() {
        if(playerManager == null)
            playerManager = new PlayerManager();
        return playerManager;
    }

    private LinkedList<Player> playerList = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    public void addPlayer(String account, int wealth) {
        lock.lock();
        Player player = new Player();
        switch (playerList.size()) {
            case 0: player.x = 1; player.y = 1; break;
            case 1: player.x = 20; player.y = 1; break;
            case 2: player.x = 1; player.y = 23; break;
            case 3: player.x = 23; player.y = 23; break;
        }
        player.account = account;
        player.wealth = wealth;
        playerList.add(player);
        lock.unlock();
    }

    public void setPlayerOnlineStatus(Boolean online, String account) {
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                x.online = online;
                lock.unlock();
                return;
            }
        }
        lock.unlock();
    }

    public void addPlayerTools(String tool, String account) {
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                x.toolList.add(tool);
                lock.unlock();
                return;
            }
        }
        lock.unlock();
    }


    public boolean checkIfUserReservedItem(String account, String item) {
        boolean result = false;
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                if(x.reservedTool.equals(item)) {
                    result = true;
                    x.reservedTool = "";
                    x.toolList.remove(item);
                }
            }
        }
        lock.unlock();
        return result;
    }

    public void setUserSlightLength(String account, int length) {
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                x.slightLength = length;
            }
        }
        lock.unlock();
    }

    public boolean reserveItem(String account, String item) {
        boolean result = false;
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                if(x.toolList.contains(item)) {
                    result = true;
                    x.reservedTool = item;
                }
            }
        }
        lock.unlock();
        return result;
    }

    public void increasePlayerWealth(int amount, String account) {
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                x.wealth += amount;
                lock.unlock();
                PlayerDataService.getPlayerDataService().setPlayerWealth(account, "" + x.wealth);
                return;
            }
        }
        lock.unlock();
    }

    public void resetPlayerWealth(int amount, String account) {
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account)) {
                x.wealth = amount;
                lock.unlock();
                PlayerDataService.getPlayerDataService().setPlayerWealth(account, "" + x.wealth);
                return;
            }
        }
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

    public void resetSwinable(String account, boolean swimable) {
        lock.lock();
        Player player = new Player();
        player.account = account;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player next = (Player) it.next();
            if(next.equals(player)) {
                next.canSwim = swimable;
                break;
            }
        }
        lock.unlock();
    }

    public boolean checkUserSwimmable(String account) {
        boolean result = false;
        lock.lock();
        for(Player x:playerList) {
            if(account.equals(x.account) && x.canSwim) {
                result = true;
            }
        }
        lock.unlock();
        return result;
    }

    public void resetDirection(String account, int direction) {
        lock.lock();
        Player player = new Player();
        player.account = account;
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            Player next = (Player) it.next();
            if(next.equals(player)) {
                next.direction = direction;
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

    public void removeAll() {
        lock.lock();
        playerList = new LinkedList<>();
        lock.unlock();
    }

}
