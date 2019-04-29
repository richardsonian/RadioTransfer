package com.rlapcs.radiotransfer.machines.encoders.abstract_encoder;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public abstract class AbstractTileEncoder extends AbstractTileMultiblockNodeWithInventory {
    public AbstractTileEncoder(int itemStackHandlerSize) {
        super(itemStackHandlerSize);
    }

    public abstract TransferType getTransferType();
}
