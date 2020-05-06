package com.rlapcs.radiotransfer.machines.antennas.abstract_antenna;

import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachine;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.material.Material;

public abstract class AbstractAntenna extends AbstractModeledMachine {
    public AbstractAntenna(Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
        super(Material.IRON, tileEntityClass);
    }
}