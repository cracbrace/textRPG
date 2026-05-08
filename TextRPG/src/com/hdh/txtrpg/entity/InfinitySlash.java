package com.hdh.txtrpg.entity;

public class InfinitySlash implements Skill {
    private final String name;
    private final int damage;
    private final int mpCost;

    public InfinitySlash(String name, int damage, int mpCost) {
        this.name = name;
        this.damage = damage;
        this.mpCost = mpCost;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(Character caster, Character target) {
        if (caster.getMp() < mpCost) {
            System.out.println("❌ 蓝量不足！无法释放【" + name + "】（需要" + mpCost + "点MP）");
            return;
        }
        caster.setMp(caster.getMp() - mpCost);
        int actualDamage = Math.max(damage - target.defense, 1);
        target.takeDamage(actualDamage);
        System.out.println("✂️ " + caster.getName() + "释放了【" + name + "】！");
        System.out.println("消耗" + mpCost + "点MP，对" + target.getName() + "造成" + actualDamage + "点伤害！");
    }
}
