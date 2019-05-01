package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNodeWithGui;
import net.minecraft.block.material.Material;

public abstract class AbstractBlockProcessor extends AbstractBlockMultiblockNodeWithGui {
    public AbstractBlockProcessor(Class<? extends AbstractTileProcessor> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}
