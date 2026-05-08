package com.hdh.txtrpg.entity;

public interface Skill {
    String getName();
    void apply(Character caster, Character target);
}
