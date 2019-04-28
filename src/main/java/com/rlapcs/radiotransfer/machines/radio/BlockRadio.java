package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockRadio extends AbstractBlockMachine {
    public BlockRadio() {
        super(Material.IRON, TileRadio.class);

        setRegistryName("radio");
        setUnlocalizedName(RadioTransfer.MODID + "." + "radio");
        setCreativeTab(CreativeTabs.MISC);
    }
}
