package com.hdh.txtrpg.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Location {
    private String name;
    private String description;
    // 存储出口：方向 -> 对应的Location对象 (例如 "north" -> Forest)
    private Map<String, Location> exits;

    // 场景里包含的物品（宝箱）
    private List<Item> items;
    // 场景里包含的敌人（遭遇战）
    private Enemy enemy;
    // 场景里的NPC
    private NPC npc;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.enemy = null; // 默认为空，可以用setEnemy添加
        this.npc = null;
    }

    // 设置出口
    public void addExit(String direction, Location location) {
        exits.put(direction, location);
    }

    // 获取出口
    public Location getExit(String direction) {
        return exits.get(direction);
    }

    // 获取所有可用方向的字符串（用于提示玩家）
    public String getExitString() {
        StringBuilder sb = new StringBuilder("可用方向: ");
        for (String dir : exits.keySet()) {
            sb.append(dir).append(" ");
        }
        return sb.toString();
    }

    // === 物品相关 ===
    public void addItem(Item item) {
        items.add(item);
    }

    // 捡起物品（简化逻辑：捡起第一个）
    public Item takeItem() {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }

    public boolean hasItem() {
        return !items.isEmpty();
    }



    // === 敌人相关 ===
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    // 敌人被击败后移除
    public void clearEnemy() {
        this.enemy = null;
    }

    // === NPC 相关 ===
    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public NPC getNpc() {
        return npc;
    }

    public boolean hasNpc() {
        return npc != null;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
}
