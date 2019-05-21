package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachineWithGui;
import net.minecraft.block.material.Material;

public abstract class AbstractBlockProcessor extends AbstractModeledMachineWithGui {
    public AbstractBlockProcessor(Class<? extends AbstractTileProcessor> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}
