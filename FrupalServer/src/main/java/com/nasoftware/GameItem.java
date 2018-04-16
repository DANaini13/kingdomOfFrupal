package com.nasoftware;

public class GameItem {
    public final String type;
    public final String name;
    public boolean visiable;

    public GameItem(String type, String name) {
        this.type = type;
        this.name = name;
        this.visiable = false;
    }

    public GameItem(GameItem copy) {
        this.type = copy.type;
        this.name = copy.name;
        this.visiable = copy.visiable;
    }

    public String toString() {
        String result = "_";
        switch (type) {
            case "water": result = "~"; break;
            case "desert": result = "*"; break;
            case "wall": result = "o"; break;
            case "forest": result = "|"; break;
            case "meadow": result = " "; break;
        }
        switch (name) {
            case "Boulder": result = "B"; break;
            case "Tree": result = "T"; break;
            case "Blackberry": result = "S"; break;
        }
        return result;
    }
}
