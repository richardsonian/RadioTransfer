package com.rlapcs.radiotransfer.common.tileEntities;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(DemoBlockTileEntity.class, new ResourceLocation(RadioTransfer.MODID, "_tileEntity")); //is this right?
        GameRegistry.registerTileEntity(TransmitterTileEntity.class, new ResourceLocation(RadioTransfer.MODID, "_tileEntity"));
    }
}
