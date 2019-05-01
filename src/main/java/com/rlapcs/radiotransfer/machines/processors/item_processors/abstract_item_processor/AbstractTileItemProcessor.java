package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.generic.capability.item.ItemPacketQueue;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractTileItemProcessor extends AbstractTileProcessor<ItemPacketQueue> {
    public static final int PROCESS_TIME = 15; //in ticks

    protected ItemPacketQueue packetQueue;
    protected int processTimeElapsed;

    public AbstractTileItemProcessor(int itemStackHandlerSize) {
        super(itemStackHandlerSize);

        packetQueue = new ItemPacketQueue() {
            @Override
            protected void onContentsChanged() {
                AbstractTileItemProcessor.this.markDirty();
            }
        };
        processTimeElapsed = 0;
    }

    public double getFractionOfProcessCompleted() {
        return (double) processTimeElapsed / (double) PROCESS_TIME;
    }

    @Override
    public ItemPacketQueue getHandler() {
        return packetQueue;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("packets")) {
            packetQueue.deserializeNBT(compound.getCompoundTag("packets"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("packets", packetQueue.serializeNBT());

        return compound;
    }

    @Override
    public TransferType getTransferType() {
        return TransferType.ITEM;
    }
}
