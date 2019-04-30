package com.rlapcs.radiotransfer.machines.encoders.item_encoder;

import com.rlapcs.radiotransfer.generic.capability.item.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.AbstractTileEncoder;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class TileItemEncoder extends AbstractTileEncoder<ItemPacketQueue> {
    public static final int INVENTORY_SIZE = 12;
    public static final double POWER_USAGE = 10;

    private ItemPacketQueue packetQueue;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);

        packetQueue = new ItemPacketQueue() {
            @Override
            protected void onContentsChanged() {
                TileItemEncoder.this.markDirty();
            }
        };
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
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }


    @Override
    public TransferType getTransferType() {
        return TransferType.ITEM;
    }
}
