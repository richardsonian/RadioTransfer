package com.rlapcs.radiotransfer.machines.antennas.basic_antenna;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.IRadioCableConnectable;
import com.rlapcs.radiotransfer.machines.antennas.abstract_antenna.AbstractAntenna;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBasicAntenna extends AbstractAntenna implements IRadioCableConnectable {
    public static final String NAME = "basic_antenna";
    private IBlockState stateForPlacement;

    public BlockBasicAntenna() {
        super(TileBasicAntenna.class);

        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));

        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        try {
            EnumFacing blockFaceInPlayerCrosshairs = placer.rayTrace(20d, 1f).sideHit;
            System.out.println("looking at " + blockFaceInPlayerCrosshairs);
            stateForPlacement = getDefaultState().withProperty(FACING, blockFaceInPlayerCrosshairs.getOpposite());
            return stateForPlacement;
        } catch (NullPointerException ex) {
            System.err.println("Player is not looking at a block.");
            ex.printStackTrace();
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, stateForPlacement);
    }

    @Override
    public boolean canConnect(EnumFacing facing) {
        if(facing == EnumFacing.DOWN) return true;
        else return false;
    }
}
