package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.multiblock.blocks.AbstractBlockMultiblockNode;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPowerSupply extends AbstractBlockMultiblockNode {
    public BlockPowerSupply() {
        super(Material.IRON, TilePowerSupply.class);

        setRegistryName("power_supply");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
