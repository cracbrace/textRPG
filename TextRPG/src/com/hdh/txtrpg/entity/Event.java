package com.hdh.txtrpg.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Event {
    private final String title;
    private final List<String> dialogues;
    private final String rewardDesc;
    private final int type;

    public Event(String title, List<String> dialogues, String rewardDesc, int type) {
        this.title = title;
        this.dialogues = dialogues;
        this.rewardDesc = rewardDesc;
        this.type = type;
    }

    public void play(Player player, Scanner scanner) {
        System.out.println("\n【" + title + "】");
        for (String d : dialogues) {
            System.out.println(d);
            System.out.print("(按回车继续)");
            scanner.nextLine();
        }
        applyReward(player);
        System.out.println("获得：" + rewardDesc);
    }

    private void applyReward(Player player) {
        switch (type) {
            case 1:
                player.getInventory().addItem(new ManaPotion("修道院的圣水", 50));
                player.setMp(player.getMp() + 10);
                break;
            case 2:
                player.getInventory().addItem(new HealthPotion("佣兵的干粮", 80));
                player.gainExp(30);
                break;
            case 3:
                player.setAttack(player.getAttack() + 5);
                player.setDefense(player.getDefense() + 3);
                break;
            case 4:
                player.getInventory().addItem(new EnhanceFruit("古代炼金药", 20));
                break;
            case 5:
                player.getInventory().addItem(new HealthPotion("巡礼者的生命药水", 30));
                player.getInventory().addItem(new ManaPotion("巡礼者的魔力药水", 30));
                player.gainExp(20);
                break;
            default:
                break;
        }
    }

    public static List<Event> presets() {
        List<Event> list = new ArrayList<>();
        {
            List<String> ds = new ArrayList<>();
            ds.add("你在路旁发现一座荒弃的修道院，断壁残垣间仍有一尊蒙尘的圣像。");
            ds.add("你拂去灰尘，心中涌起一股久违的宁静，仿佛古老的信仰仍在回应。");
            list.add(new Event("荒弃的修道院", ds, "圣水×1，MP+10", 1));
        }
        {
            List<String> ds = new ArrayList<>();
            ds.add("天色渐暗，你遇到一位在路边生火的落魄佣兵。");
            ds.add("他虽衣衫褴褛，但还是分给了你一块烤干的肉脯和一些关于生存的建议。");
            list.add(new Event("营火旁的佣兵", ds, "干粮×1，经验+30", 2));
        }
        {
            List<String> ds = new ArrayList<>();
            ds.add("这里曾是一片古战场，生锈的剑戟与折断的旗帜散落在泥土中。");
            ds.add("你凝视着这些遗物，仿佛能听到昔日金铁交鸣的战吼，斗志在心中重燃。");
            list.add(new Event("古战场的遗迹", ds, "攻击+5、防御+3", 3));
        }
        {
            List<String> ds = new ArrayList<>();
            ds.add("一位披着斗篷的炼金术士正在调试他的药剂。");
            ds.add("见你路过，他塞给你一个散发着奇异光芒的瓶子，“试作品，大概没毒。”他匆匆离去。");
            list.add(new Event("神秘的炼金术士", ds, "古代炼金药×1（四维+20）", 4));
        }
        {
            List<String> ds = new ArrayList<>();
            ds.add("路边伫立着一座简陋的神龛，是巡礼者们留下的。");
            ds.add("供奉盘里放着几瓶未开封的药水，看来是为后来者准备的。");
            list.add(new Event("巡礼者的神龛", ds, "生命药水×1、魔力药水×1、经验+20", 5));
        }
        return list;
    }
}

