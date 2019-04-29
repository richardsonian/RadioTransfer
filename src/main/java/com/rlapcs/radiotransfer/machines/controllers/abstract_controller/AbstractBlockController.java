package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNode;
import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNodeWithGui;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.material.Material;

public abstract class AbstractBlockController extends AbstractBlockMultiblockNodeWithGui {
    public AbstractBlockController(Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}
