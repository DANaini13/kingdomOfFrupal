package com.company.Game;

public class StaticGameItem {
    final String type;
    final String name;
    final int energyChange;
    boolean visiable;

    public StaticGameItem(String type, String name) {
        this.type = type;
        this.name = name;
        this.energyChange = 0;
        this.visiable = false;
    }

    public StaticGameItem(String type, String name, int energyChange) {
        this.type = type;
        this.name = name;
        this.energyChange = energyChange;
        this.visiable = false;
    }
}
