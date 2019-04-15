package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.demo.TileDemoBlock;
import com.rlapcs.radiotransfer.machines.transmitter.TileTransmitter;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileDemoBlock.class, getTeResourceLocation(ModBlocks.demoblock)); //is this right?
        GameRegistry.registerTileEntity(TileTransmitter.class, getTeResourceLocation(ModBlocks.transmitter));
    }

    private static ResourceLocation getTeResourceLocation(Block block) {
        return new ResourceLocation(RadioTransfer.MODID, block.getRegistryName().getResourcePath() + "_tileEntity");
    }
}
