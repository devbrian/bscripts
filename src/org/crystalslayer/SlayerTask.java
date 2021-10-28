package org.crystalslayer;

import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;

public enum SlayerTask {
    NONE(null, ""),
    SPIDERS( new WorldArea(
            new WorldPoint(3240, 12474, 0), new WorldPoint(3260, 12456, 0)), "spider"),
    RATS( new WorldArea(
            new WorldPoint(3235, 12446, 0), new WorldPoint(3246, 12431, 0)), "rat"),
    BATS( new WorldArea(
            new WorldPoint(3221, 12463, 0), new WorldPoint(3237, 12450, 0)), "bat");

    private WorldArea area;
    private String npcName;
    SlayerTask(WorldArea area, String name) {
        this.area = area;
        this.npcName = name;
    }

    public WorldArea getArea() {
        return this.area;
    }
    public String getNpc() {
        return this.npcName;
    }
}
