package com.nasoftware.DataLayer;

public class Tool {
    public String name;
    public boolean useable;

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        if (o instanceof Tool) {
            if(((Tool) o).name.equals(this.name))
                return true;
        }
        if (o instanceof String) {
            if(o.equals(this.name))
                return true;
        }
        return false;
    }
}
