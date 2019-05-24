package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Set;

public interface ITileClientUpdater {
    Set<EntityPlayerMP> getClientListeners();
    void doAllClientUpdates();
    default boolean addClientListener(EntityPlayerMP player) {
        boolean result = getClientListeners().add(player);
        if(result) doAllClientUpdates();
        return result;
    }
    default boolean removeClientListener(EntityPlayerMP player) {
        return getClientListeners().remove(player);
    }
}
