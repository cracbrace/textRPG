package com.hdh.txtrpg.entity;

public class EnhanceFruit implements Item {
    private final String name;
    private final int value;

    public EnhanceFruit(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(Character user) {
        user.setHp(user.getHp() + value);
        user.setMp(user.getMp() + value);
        user.setAttack(user.getAttack() + value);
        user.setDefense(user.getDefense() + value);
        System.out.println("🍎 使用了【" + name + "】，属性提升：HP+" + value + " MP+" + value + " 攻击+" + value + " 防御+" + value);
    }
}
