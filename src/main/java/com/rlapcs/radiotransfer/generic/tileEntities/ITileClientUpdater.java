package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public interface ITileClientUpdater {
    List<EntityPlayerMP> getClientListeners();
    void doAllClientUpdates();
    default boolean addClientListener(EntityPlayerMP player) {
        if(!getClientListeners().contains(player)) {
            //sendToAllPlayers(TextFormatting.GREEN + "Adding player " + player + " from tracking list for " + this, world);
            getClientListeners().add(player);
            doAllClientUpdates();
            return true;
        }
        return false;
    }
    default boolean removeClientListener(EntityPlayerMP player) {
        if(getClientListeners().contains(player)) {
            //sendToAllPlayers(TextFormatting.RED + "Removing player " + player + " from tracking list for " + this, world);
            getClientListeners().remove(player);
            return true;
        }
        return false;
    }
}
