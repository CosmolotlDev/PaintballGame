package com.cosmolotl.paintballgame.enums;

public enum GunType {

    CLASSIC_GUN(
            1,
            "Paintball Gun",
            195L
    ),
    SUB_MACHINE_GUN(
            2,
            "Sub Machine Gun",
            195L
    ),
    SHOTGUN(
            3,
                    "Shotgun",
                    995L
    ),
    SNIPER_GUN(
            4,
                    "Sniper",
                    1995L
    );

    private int customModel;
    private String displayName;
    private long coolDown;

    GunType(int customModel, String displayName, long coolDown){
        this.customModel = customModel;
        this.displayName = displayName;
        this.coolDown = coolDown;
    }

    public int getCustomModel() {
        return customModel;
    }
    public String getDisplayName(){
        return displayName;
    }
    public long getCoolDown(){
        return coolDown;
    }
}
