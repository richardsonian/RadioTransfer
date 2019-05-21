package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockRadio extends AbstractModeledMachine {
    public BlockRadio() {
        super(Material.IRON, TileRadio.class);

        setRegistryName("radio");
        setUnlocalizedName(RadioTransfer.MODID + "." + "radio");
        setCreativeTab(CreativeTabs.MISC);
    }
}
