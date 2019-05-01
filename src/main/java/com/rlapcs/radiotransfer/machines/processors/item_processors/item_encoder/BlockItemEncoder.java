package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractBlockItemProcessor;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemEncoder extends AbstractBlockItemProcessor {
    public BlockItemEncoder() {
        super(TileItemEncoder.class);

        setRegistryName("item_encoder");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
