package com.rlapcs.radiotransfer.generic.multiblock.blocks;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.registries.ModGuis;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbstractBlockMultiblockNodeWithGui extends AbstractBlockMultiblockNode {
    public AbstractBlockMultiblockNodeWithGui(Material material, Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
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
