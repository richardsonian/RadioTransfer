package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractBlockItemProcessor;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemEncoder extends AbstractBlockItemProcessor {
    public static final String NAME = "item_encoder";
    public BlockItemEncoder() {
        super(TileItemEncoder.class);


        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }
}
