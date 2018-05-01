package com.nasoftware.GameLayer;

import java.util.LinkedList;

public class Player {
    String account;
    int x = 0;
    int y = 0;
    int energy = 100;
    int wealth = 100;
    int direction = 0;
    LinkedList<String> toolList = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Player)
            return ((Player)o).account.equals(account);
        return false;
    }

}
