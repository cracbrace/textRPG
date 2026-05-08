package com.hdh.txtrpg.entity;

import com.hdh.txtrpg.util.DirectionConstant;
import com.hdh.txtrpg.util.MapConstant;
import com.hdh.txtrpg.map.MapGenerator;
import com.hdh.txtrpg.map.DefaultMapGenerator;
import com.hdh.txtrpg.map.MapData;
import java.util.HashMap;
import java.util.Map;

public class GameWorld {
    private Location startLocation;
    private char[][] mapGrid;
    private int playerX;
    private int playerY;
    private int lastSafeX;
    private int lastSafeY;
    private Map<String, Location> coordToLocation;
    private Location village, forest, cave, castle;
    private MapData mapData;
    private Map<String, Event> events;

    public GameWorld() {
        createWorld();
        initMap();
        initCoordLocationMap();
        initEvents();
    }

    private void createWorld() {
        village = new Location("新手村", "这里是宁静的村庄，虽然安全但也很无聊。");
        forest = new Location("迷雾森林", "空气中弥漫着雾气，周围偶尔传来奇怪的叫声。");
        cave = new Location("黑暗洞穴", "阴暗潮湿，看起来非常危险。");
        castle = new Location("魔王城堡", "巨大的黑色城堡，散发着压迫感。");
        village.addItem(new HealthPotion("新手药水", 50));
        forest.addItem(new EnhanceFruit("强化果实", 20));
        forest.setEnemy(Enemy.createSlime("森林史莱姆"));
        cave.setEnemy(Enemy.createSkeleton("洞穴骷髅"));
        castle.setEnemy(Enemy.createDragon("绝望黑龙"));
        village.setNpc(new NPC("老村长", "年轻的勇者啊，传说森林深处的古堡里有一条恶龙，但你现在的实力还不足以击败它。去森林里历练一番吧！"));
        cave.setNpc(new NPC("女神", "你的勇气令人赞赏。愿赐你神技，以斩尽黑暗。"));
        startLocation = village;
    }

    private void initMap() {
        MapGenerator generator = new DefaultMapGenerator();
        mapData = generator.generate();
        mapGrid = mapData.grid;
        playerX = mapData.playerX;
        playerY = mapData.playerY;
        lastSafeX = playerX;
        lastSafeY = playerY;
        mapGrid[playerY][playerX] = MapConstant.PLAYER;
    }

    private void initCoordLocationMap() {
        coordToLocation = new HashMap<>();
        for (int dy = 0; dy < 2; dy++) {
            for (int dx = 0; dx < 2; dx++) {
                coordToLocation.put((mapData.villageY + dy) + "," + (mapData.villageX + dx), village);
            }
        }
        coordToLocation.put(mapData.castleY + "," + mapData.castleX, castle);
        coordToLocation.put(mapData.forestY + "," + mapData.forestX, forest);
        coordToLocation.put(mapData.caveY + "," + mapData.caveX, cave);
    }

    private void initEvents() {
        events = new HashMap<>();
        java.util.List<String> candidates = new java.util.ArrayList<>();
        for (int y = 0; y < MapConstant.MAP_HEIGHT; y++) {
            for (int x = 0; x < MapConstant.MAP_WIDTH; x++) {
                if (mapGrid[y][x] == MapConstant.PATH) {
                    String coord = y + "," + x;
                    if (!coordToLocation.containsKey(coord) && !(y == playerY && x == playerX)) {
                        candidates.add(coord);
                    }
                }
            }
        }
        if (candidates.isEmpty()) return;
        java.util.Collections.shuffle(candidates);
        int count = 3 + (int)(Math.random() * 3); // 3~5
        java.util.List<Event> presets = Event.presets();
        for (int i = 0; i < count && i < candidates.size() && i < presets.size(); i++) {
            String coord = candidates.get(i);
            String[] parts = coord.split(",");
            int y = Integer.parseInt(parts[0]);
            int x = Integer.parseInt(parts[1]);
            mapGrid[y][x] = MapConstant.EVENT;
            events.put(coord, presets.get(i));
        }
    }



    // 移动玩家坐标（返回是否移动成功）
    public boolean movePlayer(String direction) {
        // 1. 保存原坐标（用于恢复地图字符）
        int oldX = playerX;
        int oldY = playerY;
        
        // 2. 根据方向计算新坐标
        // 兼容处理：如果是全称（north），转换为首字母/按键逻辑，或者直接处理
        // 这里为了兼容 w/a/s/d 和 north/south/west/east，我们统一识别
        String dir = direction.toLowerCase();
        
        if (dir.equals("w") || dir.equals(DirectionConstant.DIR_NORTH)) {
            playerY -= 1;
        } else if (dir.equals("s") || dir.equals(DirectionConstant.DIR_SOUTH)) {
            playerY += 1;
        } else if (dir.equals("d") || dir.equals(DirectionConstant.DIR_EAST)) {
            playerX += 1;
        } else if (dir.equals("a") || dir.equals(DirectionConstant.DIR_WEST)) {
            playerX -= 1;
        } else {
            return false; // 无效方向
        }

        // 3. 检查新坐标是否合法（不超出地图+非山脉）
        if (isValidCoord(playerY, playerX)) {
            // 3.1 恢复原坐标字符（玩家离开后显示原地形/场景）
            mapGrid[oldY][oldX] = getOriginalChar(oldY, oldX);
            // 3.2 更新新坐标字符（显示玩家@）
            mapGrid[playerY][playerX] = MapConstant.PLAYER;
            lastSafeX = oldX;
            lastSafeY = oldY;
            // 3.3 更新当前Location（根据新坐标获取）
            String newCoord = playerY + "," + playerX; // 回滚为 y,x
            if (coordToLocation.containsKey(newCoord)) {
                startLocation = coordToLocation.get(newCoord);
            }
            return true;
        } else {
            // 坐标不合法，恢复原坐标
            playerX = oldX;
            playerY = oldY;
            return false;
        }
    }

    // 检查坐标是否合法（在地图内+非山脉）
    private boolean isValidCoord(int y, int x) {
        return y >= 0 && y < MapConstant.MAP_HEIGHT
                && x >= 0 && x < MapConstant.MAP_WIDTH
                && mapGrid[y][x] != MapConstant.MOUNTAIN;
    }

    // 获取坐标的原始字符（玩家离开后显示的地形/场景）
    private char getOriginalChar(int y, int x) {
        String coord = y + "," + x; // 回滚为 y,x
        if (events != null && events.containsKey(coord)) {
            return MapConstant.EVENT;
        }
        if (coordToLocation.containsKey(coord)) {
            // 坐标对应场景，返回场景字符
            Location loc = coordToLocation.get(coord);
            if (loc.getName().contains("新手村")) return MapConstant.VILLAGE;
            if (loc.getName().contains("森林")) return MapConstant.FOREST;
            if (loc.getName().contains("洞穴")) return MapConstant.CAVE;
            if (loc.getName().contains("城堡")) return MapConstant.CASTLE;
        }
        // 非场景坐标，返回路径/空白
        return mapGrid[y][x] == MapConstant.PATH ? MapConstant.PATH : MapConstant.EMPTY;
    }

    // 获取地图网格（给MainGame显示用）
    public char[][] getMapGrid() {
        return mapGrid;
    }

    // 获取起始场景（更新为当前坐标对应的场景）
    public Location getStartLocation() {
        return startLocation;
    }

    // 获取玩家当前坐标（给MainGame显示状态用）
    public String getPlayerCoord() {
        return "(" + playerY + "," + playerX + ")";
    }

    public void escapeToLastSafePosition() {
        mapGrid[playerY][playerX] = getOriginalChar(playerY, playerX);
        playerX = lastSafeX;
        playerY = lastSafeY;
        mapGrid[playerY][playerX] = MapConstant.PLAYER;
        String coord = playerY + "," + playerX;
        if (coordToLocation.containsKey(coord)) {
            startLocation = coordToLocation.get(coord);
        }
    }

    public Location getLocationAtPlayer() {
        return coordToLocation.get(playerY + "," + playerX);
    }

    public Event getEventAtPlayer() {
        String coord = playerY + "," + playerX;
        return events != null ? events.get(coord) : null;
    }

    public void consumeEventAtPlayer() {
        String coord = playerY + "," + playerX;
        if (events != null && events.containsKey(coord)) {
            events.remove(coord);
            mapGrid[playerY][playerX] = MapConstant.PATH;
        }
    }
}
