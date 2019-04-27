package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockRadio extends AbstractBlockMachine {
    public BlockRadio() {
        super(Material.IRON, TileRadio.class);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileRadio) {
            TileEntity otherTe = world.getTileEntity(neighbor);

            if(otherTe != null) {
                ((TileRadio) te).getMultiblockController().validateMultiblockAddition(otherTe);
            }
        }
    }
}
