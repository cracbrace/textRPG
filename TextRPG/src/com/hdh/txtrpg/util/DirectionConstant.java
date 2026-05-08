package com.hdh.txtrpg.util;

/**
 * 方向常量：绑定键盘按键与方向文本/逻辑
 */
public class DirectionConstant {
    // 1. 键盘按键常量（w/a/s/d）
    public static final char KEY_MOVE_NORTH = 'w';  // 上→north
    public static final char KEY_MOVE_SOUTH = 's';  // 下→south
    public static final char KEY_MOVE_WEST = 'a';   // 左→west
    public static final char KEY_MOVE_EAST = 'd';   // 右→east

    // 2. 方向文本常量（与GameWorld.movePlayer()兼容）
    public static final String DIR_NORTH = "north";
    public static final String DIR_SOUTH = "south";
    public static final String DIR_WEST = "west";
    public static final String DIR_EAST = "east";

    // 3. 按键→方向文本的转换方法（核心：输入按键，返回对应方向）
    public static String getDirByKey(char key) {
        switch (Character.toLowerCase(key)) {  // 忽略大小写（支持W/A/S/D）
            case KEY_MOVE_NORTH:
                return DIR_NORTH;
            case KEY_MOVE_SOUTH:
                return DIR_SOUTH;
            case KEY_MOVE_WEST:
                return DIR_WEST;
            case KEY_MOVE_EAST:
                return DIR_EAST;
            default:
                return null;  // 无效按键返回null
        }
    }
}
