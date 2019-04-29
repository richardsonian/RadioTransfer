package com.rlapcs.radiotransfer.machines.decoders.abstract_decoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public abstract class AbstractTileDecoder extends AbstractTileMultiblockNodeWithInventory {
    public AbstractTileDecoder(int itemStackHandlerSize) {
        super(itemStackHandlerSize);
    }

    public abstract TransferType getTransferType();
}