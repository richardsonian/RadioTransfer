package com.rlapcs.radiotransfer.util;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Debug {
    public static void sendDebugMessage(String msg) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(msg));
    }
}
