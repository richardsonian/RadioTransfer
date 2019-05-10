package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.entity.player.EntityPlayerMP;

public interface ITileClientUpdater {
    boolean addClientListener(EntityPlayerMP player);
    boolean removeClientListener(EntityPlayerMP player);

    void doClientUpdate();
}
