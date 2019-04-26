package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.deprecated.demo.TileDemoBlock;
import com.rlapcs.radiotransfer.machines.deprecated.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines.deprecated.transmitter.TileTransmitter;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileDemoBlock.class, getTeResourceLocation(ModBlocks.demoblock));
        GameRegistry.registerTileEntity(TileTransmitter.class, getTeResourceLocation(ModBlocks.transmitter));
        GameRegistry.registerTileEntity(TileReceiver.class, getTeResourceLocation(ModBlocks.receiver));
    }

    private static ResourceLocation getTeResourceLocation(Block block) {
        return new ResourceLocation(RadioTransfer.MODID, block.getRegistryName().getResourcePath() + "_tileEntity");
    }
}
