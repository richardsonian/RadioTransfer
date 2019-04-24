package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.generic.network.messages.MessageActivateTileRadio;
import com.rlapcs.radiotransfer.generic.network.messages.MessageUpdateTileRadioFrequency;
import com.rlapcs.radiotransfer.generic.network.messages.MessageUpdateTileReceiverPriority;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkMessages {
    public static void registerMessages() {
        // Register messages here with the specified receiving side
        INSTANCE.registerMessage(MessageActivateTileRadio.Handler.class, MessageActivateTileRadio.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateTileRadioFrequency.Handler.class, MessageUpdateTileRadioFrequency.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateTileReceiverPriority.Handler.class, MessageUpdateTileReceiverPriority.class, nextID(), Side.SERVER);
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
