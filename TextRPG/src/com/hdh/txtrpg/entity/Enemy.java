package com.hdh.txtrpg.entity;

public class Enemy extends Character {
    private String enemyType;
    private int expReward;
    
    public Enemy(String name, String enemyType, int HP, int MP, int attack, int defense, int expReward) {
        super(name, HP, MP, attack, defense);

        this.enemyType = enemyType;
        this.expReward = expReward;
    }


    @Override
    public void displayStatus() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           敌人信息              ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.printf("║ 名称: %-22s ║\n", super.name);
        System.out.printf("║ 种类: %-22s ║\n", this.enemyType);
        System.out.printf("║ 生命值: %-20d ║\n", super.hp);
        System.out.printf("║ 魔法值: %-20d ║\n", super.mp);
        System.out.printf("║ 攻击力: %-20d ║\n", super.attack);
        System.out.printf("║ 防御力: %-20d ║\n", super.defense);
        System.out.printf("║ 经验值奖励: %-16d ║\n", this.expReward);
        System.out.println("╚══════════════════════════════════╝");
    }


    // 获取击败敌人后获得的经验值
    public int getExpReward() {
        return this.expReward;
    }
    
    // 获取敌人类型
    public String getEnemyType() {
        return this.enemyType;
    }

    // 工厂方法：创建哥布林
    public static Enemy createGoblin(String name) {
        return new Enemy(name, "哥布林", 50, 20, 12, 2, 30);
    }

    // 工厂方法：创建巨龙
    public static Enemy createDragon(String name) {
        return new Enemy(name, "巨龙", 280, 100, 60, 20, 200);
    }

    // 工厂方法：创建史莱姆
    public static Enemy createSlime(String name) {
        return new Enemy(name, "史莱姆", 25, 0, 8, 0, 10);
    }

    // 工厂方法：创建骷髅战士
    public static Enemy createSkeleton(String name) {
        return new Enemy(name, "骷髅战士", 70, 10, 18, 8, 45);
    }
}
