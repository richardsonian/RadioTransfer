package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractBlockItemProcessor;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemDecoder extends AbstractBlockItemProcessor {
    public static final String NAME = "item_decoder";

    public BlockItemDecoder() {
        super(TileItemDecoder.class);

        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }
}
