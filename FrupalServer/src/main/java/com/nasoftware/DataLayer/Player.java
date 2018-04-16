package com.nasoftware.DataLayer;

import java.util.LinkedList;
public class Player {
    public String account;
    public int x = 0;
    public int y = 0;
    public int energy = 100;
    public int wealth = 100;
    public LinkedList<String> toolList = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Player)
            return ((Player)o).account.equals(account);
        return false;
    }

}
