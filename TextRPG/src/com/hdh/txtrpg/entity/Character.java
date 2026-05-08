package com.hdh.txtrpg.entity;

public abstract class Character {
    // 修正1：变量名改为小驼峰（符合Java规范）
    protected String name;
    protected int hp;
    protected int mp;
    //protected int maxHp;  // 新增：最大生命值
    //protected int maxMp;  // 新增：最大魔力值
    protected int attack;
    protected int defense;

    // 构造方法修改：初始化最大生命值和魔力值
    public Character(String name, int hp, int mp, int attack, int defense) {
        this.name = name;
       // this.maxHp = Math.max(0, hp);  // 最大HP不能为负
        this.hp = hp;          // 初始HP为最大值
        //this.maxMp = Math.max(0, mp);  // 最大MP不能为负
        this.mp = mp;          // 初始MP为最大值
        this.attack = attack;
        this.defense = defense;
    }

    // getter/setter同步改小驼峰，set方法加基础校验
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }


    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }
    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = Math.max(0, mp);
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, attack);
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

//    public int getMaxHp() { return maxHp; }
//    public int getMaxMp() { return maxMp;}

        // 攻击方法：
    public void attack(Character target) {
        // 攻击方攻击 - 被攻击方防御，保底1点伤害（避免防御过高攻击无效）
        int damage = Math.max(this.attack - target.defense, 1);
        target.takeDamage(damage);
    }

    // 判断存活：基于修正后的hp
    public boolean isAlive() {
        return hp > 0;
    }

    // 承受伤害：
    public void takeDamage(int dmg) {
        // 直接扣血（防御已经在attack方法里算过了）
        this.hp -= dmg;
        // 确保hp不会低于0
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    // 抽象方法：子类实现状态展示（极简即可）
    public abstract void displayStatus();
}