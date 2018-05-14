package com.nasoftware.GameLayer;

import java.util.Iterator;
import java.util.LinkedList;

public class GameItem {
    public final String type;
    public final String name;
    public LinkedList<String> visibleList;
    public boolean visiable;

    public GameItem(String type, String name) {
        this.type = type;
        this.name = name;
        this.visibleList = new LinkedList<>();
        this.visiable = false;
    }

    public GameItem(GameItem copy) {
        this.type = copy.type;
        this.name = copy.name;
        this.visibleList = new LinkedList<>();
        Iterator it = copy.visibleList.iterator();
        while (it.hasNext()) {
            visibleList.add(it.next().toString());
        }
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
            case "bog": result = "."; break;
        }
        switch (name) {
            case "Boulder": result = "B"; break;
            case "Tree": result = "T"; break;
            case "Blackberry": result = "S"; break;
        }
        return result;
    }
}
