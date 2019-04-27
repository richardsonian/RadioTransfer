package com.rlapcs.radiotransfer.machines.encoders.abstract_encoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;

public abstract class AbstractTileEncoder extends AbstractTileMultiblockNodeWithInventory {
     public static final int INVENTORY_SIZE = 12;

    public AbstractTileEncoder() {
        super(INVENTORY_SIZE);
    }
}
