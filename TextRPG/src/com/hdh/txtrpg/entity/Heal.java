package com.hdh.txtrpg.entity;

public class Heal implements Skill{
    private String name;
    private int mpCost;
    private int healValue;
//    private int cooldown;
    public Heal(String name, int mpCost, int healValue) {
        this.name = name;
        this.mpCost = mpCost;
        this.healValue = healValue;
//        this.cooldown = cooldown;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(Character caster, Character target) {
        // 1. 校验MP是否足够
        if (caster.getMp() < mpCost) {
            System.out.println("❌ 蓝量不足！无法释放【" + name + "】（需要" + mpCost + "点MP）");
            return;
        }
        // 2. 扣减释放者MP
        caster.setMp(caster.getMp() - mpCost);
        // 3. 恢复目标生命值（无上限限制）
        target.setHp(target.getHp() + healValue);
        System.out.println("💖 " + caster.getName() + "释放了【" + name + "】！");
        System.out.println(target.getName() + "恢复了" + healValue + "点生命值！");
    }
}
