package com.rlapcs.radiotransfer.common.blocks;

import com.rlapcs.radiotransfer.*;
import com.rlapcs.radiotransfer.client.guis.GuiTransmitter;
import com.rlapcs.radiotransfer.common.tileEntities.TileTransmitter;
import net.minecraft.block.material.Material;

public class BlockTransmitter extends AbstractBlockWithGui {
    public BlockTransmitter() {
        super(Material.IRON, TileTransmitter.class, GuiTransmitter.class);

        setUnlocalizedName(RadioTransfer.MODID + ".transmitter");
        setRegistryName("transmitter");
    }

}