package com.rlapcs.radiotransfer.machines.encoders.item_encoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.ITileEncoder;

public class TileItemEncoder extends AbstractTileMultiblockNodeWithInventory implements ITileEncoder {
    public static final int INVENTORY_SIZE = 12;
    public static final double POWER_USAGE = 10;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }
}
