package com.rlapcs.radiotransfer.common.tileEntities;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(DemoBlockTileEntity.class, getTeResourceLocation(ModBlocks.demoblock)); //is this right?
        GameRegistry.registerTileEntity(TransmitterTileEntity.class, getTeResourceLocation(ModBlocks.transmitter));
    }

    private static ResourceLocation getTeResourceLocation(Block block) {
        return new ResourceLocation(RadioTransfer.MODID, block.getRegistryName() + "_tileEntity");
    }
}
