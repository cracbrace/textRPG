package com.hdh.txtrpg.entity;

public class HealthPotion implements Item{
    private String name;
    private int healthValue;// 恢复的生命值
    public HealthPotion(String name, int healthValue) {
        this.name = name;
        this.healthValue = healthValue;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(Character user) {
        // 回复HP：原HP + 回复量（复用setHP保证非负）
        int oldHp = user.getHp();
        user.setHp(oldHp + healthValue);
        int actualHeal = user.getHp() - oldHp;  // 实际恢复量
        System.out.println("✅ 使用了【" + name + "】，回复" + actualHeal + "点HP！当前HP：" + user.getHp());
    }
}

