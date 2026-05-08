package com.hdh.txtrpg.entity;

public class ManaPotion implements Item{
    private String name;
    private int manaValue;
    public ManaPotion(String name, int manaValue) {
        this.name = name;
        this.manaValue = manaValue;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void use(Character user) {
        user.setMp(user.getMp() + manaValue);
        System.out.println("✅ 使用了【" + name + "】，回复" + manaValue + "点MP！当前MP：" + user.getMp());
    }
}
