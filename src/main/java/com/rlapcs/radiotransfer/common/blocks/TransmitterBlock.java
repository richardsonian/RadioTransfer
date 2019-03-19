package com.rlapcs.radiotransfer.common.blocks;

import com.rlapcs.radiotransfer.*;
import com.rlapcs.radiotransfer.common.tileEntities.TransmitterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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

public class TransmitterBlock extends Block implements ITileEntityProvider {
    public static final int GUI_ID = 2;

    public TransmitterBlock() {
        super(Material.IRON);
        setUnlocalizedName(RadioTransfer.MODID + ".transmitter");
        setRegistryName("transmitter");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TransmitterTileEntity();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Only execute on the server
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TransmitterTileEntity)) {
            return false;
        }
        playerIn.openGui(RadioTransfer.instance, GUI_ID, worldIn, pos.getX(), pos.getY(),
                pos.getZ());
        return true;
    }
}