package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.antennas.basic_antenna.TileBasicAntenna;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        //multiblock radio
        GameRegistry.registerTileEntity(TileRadio.class, getTeResourceLocation(ModBlocks.radio));

        GameRegistry.registerTileEntity(TileTxController.class, getTeResourceLocation(ModBlocks.tx_controller));
        GameRegistry.registerTileEntity(TileRxController.class, getTeResourceLocation(ModBlocks.rx_controller));

        GameRegistry.registerTileEntity(TileItemEncoder.class, getTeResourceLocation(ModBlocks.item_encoder));
        GameRegistry.registerTileEntity(TileItemDecoder.class, getTeResourceLocation(ModBlocks.item_decoder));

        GameRegistry.registerTileEntity(TilePowerSupply.class, getTeResourceLocation(ModBlocks.power_supply));

        GameRegistry.registerTileEntity(TileBasicAntenna.class, getTeResourceLocation(ModBlocks.basic_antenna));
    }

    private static ResourceLocation getTeResourceLocation(Block block) {
        return new ResourceLocation(RadioTransfer.MODID, block.getRegistryName().getResourcePath() + "_tileEntity");
    }
}
