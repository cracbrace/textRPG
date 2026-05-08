package com.hdh.txtrpg.entity;

public class Blitz implements Skill{
    private String name;
    private int damage;
    private int mpCost;
    //    private int cooldown;
    public Blitz(String name, int damage, int mpCost) {
        this.name = name;
        this.damage = damage;
        this.mpCost = mpCost;
//        this.cooldown = cooldown;
    }

    @Override
    public String getName() {return name;}

    @Override
    public void apply(Character caster, Character target) {
        // 1. 校验MP是否足够
        if (caster.getMp() < mpCost) {
            System.out.println("❌ 蓝量不足！无法释放【" + name + "】（需要" + mpCost + "点MP）");
            return;
        }
        // 2. 扣减释放者MP
        caster.setMp(caster.getMp() - mpCost);
        // 3. 计算伤害（保底1点，扣目标防御）
        int actualDamage = Math.max(damage - target.defense, 1);
        target.takeDamage(actualDamage);
        // 4. 提示信息
        System.out.println("⚡️ " + caster.getName() + "释放了【" + name + "】！");
        System.out.println("消耗" + mpCost + "点MP，对" + target.getName() + "造成" + actualDamage + "点伤害！");
    }

}
