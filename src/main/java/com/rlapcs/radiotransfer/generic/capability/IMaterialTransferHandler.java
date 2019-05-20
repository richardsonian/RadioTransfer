package com.rlapcs.radiotransfer.generic.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface IMaterialTransferHandler<MATERIAL, PACKET> extends ITransferHandler, INBTSerializable<NBTTagCompound> {
    boolean canReceiveDump(PACKET packet);

    /*Removes the index if valid */
    PACKET getIndex(int index);
    PACKET peekIndex(int index);
    List<PACKET> getAsList();

    MATERIAL add(MATERIAL resources);
    MATERIAL add(MATERIAL resources, int maxAmount);
    MATERIAL peekNextPacket(int maxAmount);
    MATERIAL getNextPacket(int maxAmount);
    boolean canAddAll(MATERIAL resources, int maxAmount);
    boolean canAddAny(MATERIAL resources);

    int size();
    void move(int fromIndex, int toIndex);

    @Override
    NBTTagCompound serializeNBT();
    @Override
    void deserializeNBT(NBTTagCompound nbt);

    default boolean validateIndex(int index) {
        return (index > 0) && (index < size());
    }

    public static interface Packet<MATERIAL> {
        MATERIAL getMaterial();
        boolean isEmpty();
        int getQuantity();
        Packet copy();
    }
}
