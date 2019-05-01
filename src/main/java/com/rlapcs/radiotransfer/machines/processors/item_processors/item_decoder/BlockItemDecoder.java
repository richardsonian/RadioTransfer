package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractBlockItemProcessor;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemDecoder extends AbstractBlockItemProcessor {
    public BlockItemDecoder() {
        super(TileItemDecoder.class);

        setRegistryName("item_decoder");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
