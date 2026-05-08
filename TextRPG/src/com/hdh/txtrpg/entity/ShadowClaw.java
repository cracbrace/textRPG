package com.hdh.txtrpg.entity;

public class ShadowClaw implements Skill {
    private final String name = "暗影裂爪";
    private int damage = 35;
    private int mpCost = 8;
    private int mpDrain = 8;

    public ShadowClaw() {}

    public ShadowClaw(int damage, int mpCost, int mpDrain) {
        this.damage = damage;
        this.mpCost = mpCost;
        this.mpDrain = mpDrain;
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
        int actualDamage = Math.max(damage - (int)(target.getDefense() * 0.5), 10);
        target.takeDamage(actualDamage);
        int drain = Math.min(mpDrain, target.getMp());
        target.setMp(target.getMp() - drain);
        System.out.println("🩸 " + caster.getName() + "施放了【" + name + "】！");
        System.out.println("利爪撕裂空气，造成" + actualDamage + "点伤害，并吸走你" + drain + "点MP！");
    }
}
