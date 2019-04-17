package com.rlapcs.radiotransfer.generic.blocks;


import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.registries.*;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//note: GUI is associated through a GuiEntry in ModGuis
public abstract class AbstractBlockMachineWithGui extends AbstractBlockMachine {

    public AbstractBlockMachineWithGui(Material material, Class<? extends TileEntity> tileEntityClass) {
        super(material, tileEntityClass);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(tileEntityClass.isInstance(te))) {
            return false;
        }

        playerIn.openGui(RadioTransfer.instance, ModGuis.getGuiIDFromTileEntityClass(tileEntityClass), worldIn, pos.getX(), pos.getY(),
                pos.getZ());
        return true;
    }
}
