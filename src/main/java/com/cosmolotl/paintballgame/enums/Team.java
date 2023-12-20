package com.cosmolotl.paintballgame.enums;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;

public enum Team {

    PURPLE(
            ChatColor.DARK_PURPLE,
            Color.PURPLE,
            2,
            Material.PURPLE_CONCRETE
    ),
    BLUE(
            ChatColor.BLUE,
            Color.BLUE,
            3,
            Material.BLUE_CONCRETE
    ),
    GREEN(
            ChatColor.GREEN,
            Color.GREEN,
            4,
            Material.LIME_CONCRETE
    ),
    YELLOW(
            ChatColor.YELLOW,
            Color.YELLOW,
            5,
            Material.YELLOW_CONCRETE
    );

    private ChatColor chatColor;
    private Color color;
    private int bulletModel;
    private Material block;

    Team (ChatColor chatColor, Color color, int bulletModel, Material block){
        this.chatColor = chatColor;
        this.color = color;
        this.bulletModel = bulletModel;
        this.block = block;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
    public Color getColor(){
        return color;
    }
    public int getBulletModel() { return bulletModel; }
    public Material getBlock() { return block; }
}
