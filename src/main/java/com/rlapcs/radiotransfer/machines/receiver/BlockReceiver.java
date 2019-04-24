package com.rlapcs.radiotransfer.machines.receiver;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachineWithGui;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockReceiver extends AbstractBlockMachineWithGui {
    public BlockReceiver() {
        super(Material.IRON, TileReceiver.class);

        setUnlocalizedName(RadioTransfer.MODID + ".receiver");
        setRegistryName("receiver");
        setCreativeTab(CreativeTabs.MISC);
    }

}
