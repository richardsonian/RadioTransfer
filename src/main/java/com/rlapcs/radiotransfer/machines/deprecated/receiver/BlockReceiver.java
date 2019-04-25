package com.rlapcs.radiotransfer.machines.deprecated.receiver;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachineWithGui;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockReceiver extends AbstractBlockMachineWithGui {
    public BlockReceiver() {
        super(Material.IRON, TileReceiver.class);

        setUnlocalizedName(RadioTransfer.MODID + ".receiver");
        setRegistryName("receiver");
        setCreativeTab(CreativeTabs.MISC);
    }

}
