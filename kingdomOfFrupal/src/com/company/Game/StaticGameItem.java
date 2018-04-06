package com.company.Game;

public class StaticGameItem {
    final String type;
    final String displayName;
    final int energyChange;
    boolean visiable;

    public StaticGameItem(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
        this.energyChange = 0;
        this.visiable = false;
    }

    public StaticGameItem(String type, String displayName, int energyChange) {
        this.type = type;
        this.displayName = displayName;
        this.energyChange = energyChange;
        this.visiable = false;
    }
}
