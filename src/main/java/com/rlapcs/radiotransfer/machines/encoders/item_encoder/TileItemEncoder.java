package com.rlapcs.radiotransfer.machines.encoders.item_encoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.AbstractTileEncoder;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public class TileItemEncoder extends AbstractTileEncoder {
    public static final int INVENTORY_SIZE = 12;
    public static final double POWER_USAGE = 10;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }


    @Override
    public TransferType getTransferType() {
        return TransferType.ITEM;
    }
}
