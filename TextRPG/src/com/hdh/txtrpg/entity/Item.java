package com.hdh.txtrpg.entity;

public interface Item {
    String getName();// 获取物品名称
    void use(Character user);// 使用道具(角色)
}
