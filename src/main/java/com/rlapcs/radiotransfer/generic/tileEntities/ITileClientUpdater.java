package com.rlapcs.radiotransfer.generic.tileEntities;

import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.List;

public interface ITileClientUpdater {
    List<EntityPlayerMP> getClientListeners();
    default boolean addClientListener(EntityPlayerMP player) {
        if(!getClientListeners().contains(player)) {
            //sendToAllPlayers(TextFormatting.GREEN + "Adding player " + player + " from tracking list for " + this, world);
            getClientListeners().add(player);
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
