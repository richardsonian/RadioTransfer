package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachine;
import com.rlapcs.radiotransfer.generic.blocks.AbstractModeledMachineWithGui;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRadio extends AbstractModeledMachine {
    public BlockRadio() {
        super(Material.IRON, TileRadio.class);

        setRegistryName("radio");
        setUnlocalizedName(RadioTransfer.MODID + "." + "radio");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) { //ONLY on client
            TileEntity te = worldIn.getTileEntity(pos);
            Minecraft.getMinecraft().displayGuiScreen(new GuiRadio((TileRadio) te));
        }

        return false; //didn't do anything
    }
}
