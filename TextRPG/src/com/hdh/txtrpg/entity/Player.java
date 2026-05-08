package com.hdh.txtrpg.entity;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    //Player特有字段
    private int level;
    private int exp;
    private int expToNextLevel = 50;
    private Inventory inventory = new Inventory();
    private List<Skill> skills = new ArrayList<>();

    public Player(String name, int hp, int mp, int attack, int defense) {
        //父类字段赋值
       super(name, hp, mp  , attack, defense);
        //Player特有字段赋值
        this.level = 1;
        this.exp = 0; // 初始化经验值为0

    }

    // 重写父类的抽象方法
    @Override
    public void displayStatus() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           玩家状态                 ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.printf("║ 姓名: %-24s ║\n", super.name);
        System.out.printf("║ 等级: %-24d ║\n", this.level);
        System.out.printf("║ 经验值: %-20s ║\n" , this.exp + "/" + this.expToNextLevel);
        System.out.printf("║ 生命值: %-20d ║\n", super.hp);
        System.out.printf("║ 魔力值: %-20d ║\n", super.mp);
        System.out.printf("║ 攻击力: %-20d ║\n", super.attack);
        System.out.printf("║ 防御力: %-20d ║\n", super.defense);
        System.out.println("╚══════════════════════════════════╝");
    }


    // Player特有方法
    // 获取经验
    public void gainExp(int exp) {
        this.exp += exp;
        // 判断是否升级
        while (this.exp >= expToNextLevel) {
            levelUp(); // 升级
        }
    }
    // 升级
    public void levelUp() {
        this.exp -= expToNextLevel;
        this.level++;

        // 升级后的属性提升
        int hpAdd = 20;
        int mpAdd = 10;
        int attackAdd = 5;
        int defenseAdd = 3;

        // 提升属性
        this.hp += hpAdd;
        this.mp += mpAdd;
        this.attack += attackAdd;
        this.defense += defenseAdd;

        // 升级提示
        System.out.println("🎉 恭喜升级！当前等级：" + level);
        System.out.println("属性提升：HP+" + hpAdd + " MP+" + mpAdd + " 攻击+" + attackAdd + " 防御+" + defenseAdd);

        //后续升级
        this.expToNextLevel = (int) (expToNextLevel * 1.2);
        hpAdd += 20;
        mpAdd += 10;
        attackAdd = (int)(attackAdd * 1.1);
        defenseAdd = (int)(defenseAdd * 1.1);
    }


    // get
    public int getLevel() {
        return this.level;
    }
    public int getExp() {
        return this.exp;
    }
    public int getExpToNextLevel() {
        return this.expToNextLevel;
    }

    // Inventory:对外暴露背包方法（给Main调用）
    public Inventory getInventory() {
        return inventory;
    }

    // 检查是否已掌握某技能（按名称）
    public boolean hasSkill(String name) {
        for (Skill s : skills) {
            if (s.getName().equals(name)) return true;
        }
        return false;
    }

    // Skill:添加技能
    public void addSkill(Skill skill) {
        skills.add(skill);
        System.out.println("🔮 你学会了技能：【" + skill.getName() + "】");
    }

    // Skill:展示技能菜单（给Main调用）
    public void displaySkills() {
        if (skills.isEmpty()) {
            System.out.println("🔮 未掌握任何技能！");
            return;
        }
        System.out.println("===== 技能列表 =====");
        for (int i = 0; i < skills.size(); i++) {
            System.out.println(i + ". " + skills.get(i).getName());
        }
    }

    // Skill:使用指定索引的技能
    public void useSkill(int index, Character target) {
        if (index < 0 || index >= skills.size()) {
            System.out.println("❌ 技能索引无效！");
            return;
        }
        Skill skillToUse = skills.get(index);
        // 多态核心：调用Skill接口的apply方法，自动执行子类逻辑
        skillToUse.apply(this, target);
    }
}


