package com.rlapcs.radiotransfer.machines.decoders.item_decoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.decoders.abstract_decoder.ITileDecoder;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public class TileItemDecoder extends AbstractTileMultiblockNodeWithInventory implements ITileDecoder {
    public static final int INVENTORY_SIZE = 12;
    public static final double POWER_USAGE = 10;

    public TileItemDecoder() {
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
