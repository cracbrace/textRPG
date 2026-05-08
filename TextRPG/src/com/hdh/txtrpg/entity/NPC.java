package com.hdh.txtrpg.entity;

/**
 * 非玩家角色 (Non-Player Character)
 * 目前仅支持基础对话功能
 */
public class NPC {
    private String name;
    private String dialogue; // 默认对话

    public NPC(String name, String dialogue) {
        this.name = name;
        this.dialogue = dialogue;
    }

    public String getName() {
        return name;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }
}
