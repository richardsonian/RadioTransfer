package com.rlapcs.radiotransfer.util;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Debug {
    public static void sendDebugMessage(String msg) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(msg));
    }

    public static void sendToAllPlayers(String msg, World world) {
        world.playerEntities.forEach(p -> p.sendMessage(new TextComponentString(msg)));
    }
}
