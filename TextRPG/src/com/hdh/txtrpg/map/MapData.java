package com.hdh.txtrpg.map;

public class MapData {
    public final char[][] grid;
    public final int villageX;
    public final int villageY;
    public final int castleX;
    public final int castleY;
    public final int forestX;
    public final int forestY;
    public final int caveX;
    public final int caveY;
    public final int playerX;
    public final int playerY;
    public MapData(char[][] grid, int villageX, int villageY, int castleX, int castleY, int forestX, int forestY, int caveX, int caveY) {
        this.grid = grid;
        this.villageX = villageX;
        this.villageY = villageY;
        this.castleX = castleX;
        this.castleY = castleY;
        this.forestX = forestX;
        this.forestY = forestY;
        this.caveX = caveX;
        this.caveY = caveY;
        this.playerX = villageX;//玩家初始坐标
        this.playerY = villageY;
    }
}
