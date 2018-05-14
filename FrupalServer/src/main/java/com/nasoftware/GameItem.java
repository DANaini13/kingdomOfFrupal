package com.nasoftware;

import java.util.Iterator;
import java.util.LinkedList;

public class GameItem {
    public final String type;
    public String name;
    public LinkedList<String> visibleList;

    public GameItem(String type, String name) {
        this.type = type;
        this.name = name;
        this.visibleList = new LinkedList<>();
    }

    public GameItem(GameItem copy) {
        this.type = copy.type;
        this.name = copy.name;
        Iterator it = copy.visibleList.iterator();
        while (it.hasNext()) {
            visibleList.add(it.next().toString());
        }
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
            case "Boulder": result += "B"; break;
            case "Tree": result += "T"; break;
            case "Blackberry": result += "S"; break;
            case "Diamond": result += "D"; break;
            case "PowerBar": result += "="; break;
            case "Chest1": result += "1"; break;
            case "Binoculars": result = "oo"; break;
            case "Boat": result += "V"; break;
            case "Rock": result += "R"; break;
            case "Chest2": result += "2"; break;
            case "Jack Hammer": result += "J"; break;
            case "Hammer and Chisel": result += "H"; break;
            case "Chain Saw": result += "C"; break;
            case "Axe": result += "A"; break;
            case "Shears": result += "E"; break;
            case "Pruning Saw": result += "P"; break;
            case "TClue": result += "t"; break;
            case "FClue": result += "f"; break;
            default: result += " ";
        }
        return result;
    }
}
