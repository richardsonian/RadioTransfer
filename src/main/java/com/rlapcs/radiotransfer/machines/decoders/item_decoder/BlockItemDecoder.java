package com.rlapcs.radiotransfer.machines.decoders.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.decoders.abstract_decoder.AbstractBlockDecoder;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemDecoder extends AbstractBlockDecoder {
    public BlockItemDecoder() {
        super(TileItemDecoder.class);

        setRegistryName("item_decoder");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
