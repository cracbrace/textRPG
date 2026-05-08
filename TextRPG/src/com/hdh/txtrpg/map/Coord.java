package com.hdh.txtrpg.map;

public class Coord {
    public final int y;
    public final int x;
    public Coord(int y, int x) {
        this.y = y;
        this.x = x;
    }
    public String key() {
        return y + "," + x;
    }
}
