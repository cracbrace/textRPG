package com.hdh.txtrpg.map;

import com.hdh.txtrpg.util.MapConstant;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {
    public static boolean isReachable(char[][] grid, int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[MapConstant.MAP_HEIGHT][MapConstant.MAP_WIDTH];
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{startY, startX});
        visited[startY][startX] = true;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] c = q.poll();
            int cy = c[0], cx = c[1];
            if (cy == targetY && cx == targetX) return true;
            for (int[] d : dirs) {
                int ny = cy + d[0], nx = cx + d[1];
                if (ny >= 0 && ny < MapConstant.MAP_HEIGHT && nx >= 0 && nx < MapConstant.MAP_WIDTH
                        && !visited[ny][nx] && grid[ny][nx] != MapConstant.MOUNTAIN) {
                    visited[ny][nx] = true;
                    q.offer(new int[]{ny, nx});
                }
            }
        }
        return false;
    }
}
