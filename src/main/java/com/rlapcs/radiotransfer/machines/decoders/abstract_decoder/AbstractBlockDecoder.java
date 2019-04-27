package com.rlapcs.radiotransfer.machines.decoders.abstract_decoder;

import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNode;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.material.Material;

public abstract class AbstractBlockDecoder extends AbstractBlockMultiblockNode {
    public AbstractBlockDecoder(Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}
