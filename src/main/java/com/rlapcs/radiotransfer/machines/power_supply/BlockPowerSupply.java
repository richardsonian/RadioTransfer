package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNode;
import net.minecraft.block.material.Material;

public class BlockPowerSupply extends AbstractBlockMultiblockNode {
    public BlockPowerSupply() {
        super(Material.IRON, TilePowerSupply.class);
    }
}
