package com.rlapcs.radiotransfer.machines.encoders.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.AbstractBlockEncoder;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItemEncoder extends AbstractBlockEncoder {
    public BlockItemEncoder() {
        super(TileItemEncoder.class);

        setRegistryName("item_encoder");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
