package com.hdh.txtrpg.map;

import com.hdh.txtrpg.util.MapConstant;
import java.util.Random;

public class DefaultMapGenerator implements MapGenerator {
    private final Random random = new Random();
    public MapData generate() {
        char[][] grid = new char[MapConstant.MAP_HEIGHT][MapConstant.MAP_WIDTH];
        for (int y = 0; y < MapConstant.MAP_HEIGHT; y++) {
            for (int x = 0; x < MapConstant.MAP_WIDTH; x++) {
                grid[y][x] = MapConstant.PATH;
            }
        }
        for (int x = 0; x < MapConstant.MAP_WIDTH; x++) {
            grid[0][x] = MapConstant.MOUNTAIN;
            grid[MapConstant.MAP_HEIGHT - 1][x] = MapConstant.MOUNTAIN;
        }
        for (int y = 0; y < MapConstant.MAP_HEIGHT; y++) {
            grid[y][0] = MapConstant.MOUNTAIN;
            grid[y][MapConstant.MAP_WIDTH - 1] = MapConstant.MOUNTAIN;
        }
        int total = MapConstant.MAP_HEIGHT * MapConstant.MAP_WIDTH;
        int count = (int)(total * 0.2);
        for (int i = 0; i < count; i++) {
            int ry = random.nextInt(MapConstant.MAP_HEIGHT);
            int rx = random.nextInt(MapConstant.MAP_WIDTH);
            if (grid[ry][rx] == MapConstant.PATH) {
                grid[ry][rx] = MapConstant.MOUNTAIN;
            }
        }
        int castleX = MapConstant.MAP_WIDTH - 2;
        int castleY = 1 + random.nextInt(MapConstant.MAP_HEIGHT - 2);
        grid[castleY][castleX] = MapConstant.CASTLE;
        int bestX = 1, bestY = 1, maxDist = -1;
        for (int y = 1; y < MapConstant.MAP_HEIGHT - 2; y++) {
            for (int x = 1; x < MapConstant.MAP_WIDTH - 2; x++) {
                int dist = Math.abs(x - castleX) + Math.abs(y - castleY);
                if (dist > maxDist) {
                    maxDist = dist;
                    bestX = x;
                    bestY = y;
                }
            }
        }
        int villageX = bestX, villageY = bestY;
        for (int dy = 0; dy < 2; dy++) {
            for (int dx = 0; dx < 2; dx++) {
                grid[villageY + dy][villageX + dx] = MapConstant.VILLAGE;
            }
        }
        int forestX, forestY;
        do {
            forestX = 1 + random.nextInt(MapConstant.MAP_WIDTH - 2);
            forestY = 1 + random.nextInt(MapConstant.MAP_HEIGHT - 2);
        } while (grid[forestY][forestX] != MapConstant.PATH);
        grid[forestY][forestX] = MapConstant.FOREST;
        int caveX, caveY;
        do {
            caveX = 1 + random.nextInt(MapConstant.MAP_WIDTH - 2);
            caveY = 1 + random.nextInt(MapConstant.MAP_HEIGHT - 2);
        } while (grid[caveY][caveX] != MapConstant.PATH);
        grid[caveY][caveX] = MapConstant.CAVE;
        if (!PathFinder.isReachable(grid, villageX, villageY, castleX, castleY)) {
            int cx = villageX;
            int cy = villageY;
            while (cx != castleX || cy != castleY) {
                if (cx != castleX) {
                    cx += (castleX > cx) ? 1 : -1;
                } else if (cy != castleY) {
                    cy += (castleY > cy) ? 1 : -1;
                }
                if (grid[cy][cx] == MapConstant.MOUNTAIN) {
                    grid[cy][cx] = MapConstant.PATH;
                }
            }
        }
        return new MapData(grid, villageX, villageY, castleX, castleY, forestX, forestY, caveX, caveY);
    }
}
