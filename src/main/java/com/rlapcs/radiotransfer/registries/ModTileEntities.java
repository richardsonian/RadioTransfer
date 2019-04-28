package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.decoders.item_decoder.TileItemDecoder;
import com.rlapcs.radiotransfer.machines._deprecated.demo.TileDemoBlock;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.TileTransmitter;
import com.rlapcs.radiotransfer.machines.encoders.item_encoder.TileItemEncoder;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities() {
        //demo
        GameRegistry.registerTileEntity(TileDemoBlock.class, getTeResourceLocation(ModBlocks.demoblock));
        GameRegistry.registerTileEntity(TileTransmitter.class, getTeResourceLocation(ModBlocks.transmitter));
        GameRegistry.registerTileEntity(TileReceiver.class, getTeResourceLocation(ModBlocks.receiver));

        //multiblock radio
        GameRegistry.registerTileEntity(TileRadio.class, getTeResourceLocation(ModBlocks.radio));

        GameRegistry.registerTileEntity(TileTxController.class, getTeResourceLocation(ModBlocks.tx_controller));
        GameRegistry.registerTileEntity(TileRxController.class, getTeResourceLocation(ModBlocks.rx_controller));

        GameRegistry.registerTileEntity(TileItemEncoder.class, getTeResourceLocation(ModBlocks.item_encoder));
        GameRegistry.registerTileEntity(TileItemDecoder.class, getTeResourceLocation(ModBlocks.item_decoder));

        GameRegistry.registerTileEntity(TilePowerSupply.class, getTeResourceLocation(ModBlocks.power_supply));
    }

    private static ResourceLocation getTeResourceLocation(Block block) {
        return new ResourceLocation(RadioTransfer.MODID, block.getRegistryName().getResourcePath() + "_tileEntity");
    }
}
