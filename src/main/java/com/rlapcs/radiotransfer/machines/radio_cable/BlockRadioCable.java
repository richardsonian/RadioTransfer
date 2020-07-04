package com.rlapcs.radiotransfer.machines.radio_cable;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.IRadioCableConnectable;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.xml.soap.Text;

public class BlockRadioCable extends Block implements ITileEntityProvider, IRadioCableConnectable {
    public static final String REG_NAME = "radio_cable";

    public BlockRadioCable() {
        super(Material.IRON);
        setRegistryName(REG_NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + REG_NAME);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileRadioCable();
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~Updating Logic~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //on place is handled in tileentity to ensure it is loaded

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileRadioCable && !te.isInvalid()) {
            TileRadioCable tile = (TileRadioCable) te;
            Debug.sendToAllPlayers(String.format("%sCable@(%d, %d, %d) %s neighbor changed %s(%s)", TextFormatting.GRAY, pos.getX(), pos.getY(), pos.getZ(), TextFormatting.AQUA, TextFormatting.RESET, (te.getWorld().isRemote ? "client" : "server")), te.getWorld());
            if(!tile.getWorld().isRemote) {
                tile.onNeighborChange(neighbor);
            }
        }else {
            Debug.sendDebugMessage(TextFormatting.RED + "[ERROR] could not find te.");
        }
    }

    //debug


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileRadioCable && !te.isInvalid()) {
            TileRadioCable tile = (TileRadioCable) te;
            Debug.sendToAllPlayers(String.format("%sCable@(%d, %d, %d) connected: %s %s(%s)", TextFormatting.GRAY, pos.getX(), pos.getY(), pos.getZ(), tile.getConnectionsAsString(), TextFormatting.RESET, (world.isRemote ? "client" : "server")), world);
        }
        return true;
    }

    @Override
    public boolean canConnect(EnumFacing facing) {
        return true;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~Block Rendering Options~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        // Bind our TESR to our tile entity
        ClientRegistry.bindTileEntitySpecialRenderer(TileRadioCable.class, new RendererRadioCable());
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false; //fill this in differently?
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
}
