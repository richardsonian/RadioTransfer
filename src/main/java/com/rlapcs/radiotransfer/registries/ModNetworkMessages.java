package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientDumpablePackets;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientMultiblockNodeRegistered;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.generic.network.messages.toServer.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkMessages {
    public static void registerMessages() {
        // Register messages here with the specified receiving side
        INSTANCE.registerMessage(MessageChangeTileTxControllerMode.Handler.class, MessageChangeTileTxControllerMode.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(MessageChangeTileRxControllerPriority.Handler.class, MessageChangeTileRxControllerPriority.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateTileControllerFrequency.Handler.class, MessageUpdateTileControllerFrequency.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(MessageActivateTileController.Handler.class, MessageActivateTileController.class, nextID(), Side.SERVER);

        INSTANCE.registerMessage(MessageUpdateClientPacketQueue.Handler.class, MessageUpdateClientPacketQueue.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(MessageAddClientListener.Handler.class, MessageAddClientListener.class, nextID(), Side.SERVER);

        INSTANCE.registerMessage(MessageUpdateClientMultiblockNodeRegistered.Handler.class, MessageUpdateClientMultiblockNodeRegistered.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(MessageUpdateClientDumpablePackets.Handler.class, MessageUpdateClientDumpablePackets.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(MessageChangePacketPriority.Handler.class, MessageChangePacketPriority.class, nextID(), Side.SERVER);
    }

    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public ModNetworkMessages() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void init(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }
}
