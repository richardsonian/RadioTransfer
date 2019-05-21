package com.rlapcs.radiotransfer.generic.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelBlock {
    @SideOnly(Side.CLIENT)
    boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side);

    boolean isBlockNormalCube(IBlockState blockState);

    boolean isOpaqueCube(IBlockState blockState);
}
