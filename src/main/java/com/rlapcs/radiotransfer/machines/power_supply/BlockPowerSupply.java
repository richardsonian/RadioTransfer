package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachineWithGui;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPowerSupply extends AbstractModeledMachineWithGui {
    public static final String NAME = "power_supply";

    public BlockPowerSupply() {
        super(Material.IRON, TilePowerSupply.class);

        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }
}
