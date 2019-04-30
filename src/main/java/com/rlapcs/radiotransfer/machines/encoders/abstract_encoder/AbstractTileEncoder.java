package com.rlapcs.radiotransfer.machines.encoders.abstract_encoder;

import com.rlapcs.radiotransfer.generic.capability.item.ITransferHandler;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractTileEncoder<T extends ITransferHandler> extends AbstractTileMultiblockNodeWithInventory {

    public AbstractTileEncoder(int itemStackHandlerSize) {
        super(itemStackHandlerSize);
    }


    public abstract TransferType getTransferType();
    public abstract T getHandler();
}
