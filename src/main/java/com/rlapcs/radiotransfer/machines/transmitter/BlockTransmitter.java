package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.*;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockWithGui;
import net.minecraft.block.material.Material;

public class BlockTransmitter extends AbstractBlockWithGui {
    public BlockTransmitter() {
        super(Material.IRON, TileTransmitter.class, GuiTransmitter.class);

        setUnlocalizedName(RadioTransfer.MODID + ".transmitter");
        setRegistryName("transmitter");
    }

}