package com.hdh.txtrpg.entity;

public class DragonBreath implements Skill {
    private final String name = "龙焰吐息";
    private int damage = 50;
    private int mpCost = 12;

    public DragonBreath() {}

    public DragonBreath(int damage, int mpCost) {
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
            System.out.println("❌ " + caster.getName() + "的魔力不足，无法释放【" + name + "】");
            return;
        }
        caster.setMp(caster.getMp() - mpCost);
        int pierce = Math.max((int)(target.getDefense() * 0.2), 0);
        int actualDamage = Math.max(damage - pierce, 20);
        target.takeDamage(actualDamage);
        System.out.println("🔥 " + caster.getName() + "施放了【" + name + "】！");
        System.out.println("炽烈龙焰吞没你，造成" + actualDamage + "点伤害！");
    }
}
