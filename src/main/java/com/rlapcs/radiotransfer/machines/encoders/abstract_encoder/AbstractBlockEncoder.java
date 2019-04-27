package com.rlapcs.radiotransfer.machines.encoders.abstract_encoder;

import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNode;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.material.Material;

public class AbstractBlockEncoder extends AbstractBlockMultiblockNode {
    public AbstractBlockEncoder(Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}
