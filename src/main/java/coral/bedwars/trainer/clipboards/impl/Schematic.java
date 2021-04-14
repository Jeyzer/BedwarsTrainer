package coral.bedwars.trainer.clipboards.impl;

import lombok.Getter;

@Getter
public class Schematic {

    private final byte[] blocks, data;
    private final short width, length, height;

    public Schematic(byte[] blocks, byte[] data, short width, short length, short height) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
    }
}