package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachine;
import net.minecraft.block.material.Material;

public class BlockRadio extends AbstractBlockMachine {
    public BlockRadio() {
        super(Material.IRON, TileRadio.class);
    }
}
