package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.capability.ITransferHandler;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public abstract class AbstractTileProcessor<T extends ITransferHandler> extends AbstractTileMultiblockNodeWithInventory {
    public AbstractTileProcessor(int itemStackHandlerSize) {
        super(itemStackHandlerSize);
    }

    public abstract T getHandler();
    public abstract ProcessorType getProcessorType();
    public abstract TransferType getTransferType();
}
