package com.hdh.txtrpg.util;

/**
 * 地图常量：统一管理地形字符、场景字符
 */
public class MapConstant {
    // 地形字符
    public static final char PLAYER = '@';    // 玩家
    public static final char MOUNTAIN = '#';  // 山脉（不可通行）
    public static final char PATH = '.';      // 路径（可通行）
    public static final char EMPTY = ' ';     // 空白区域

    // 场景对应字符（与Location关联）
    public static final char VILLAGE = 'V';   // 新手村
    public static final char FOREST = 'F';    // 迷雾森林
    public static final char CAVE = 'C';      // 黑暗洞穴
    public static final char CASTLE = 'T';    // 魔王城堡
    public static final char EVENT = '?';     // 地图事件

    // 地图尺寸（可调整）
    public static final int MAP_WIDTH = 12;   // 地图宽度（列数）
    public static final int MAP_HEIGHT = 6;   // 地图高度（行数）
}
