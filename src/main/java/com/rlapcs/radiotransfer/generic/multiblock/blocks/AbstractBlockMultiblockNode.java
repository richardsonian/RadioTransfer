package com.rlapcs.radiotransfer.generic.multiblock.blocks;

import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachine;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class AbstractBlockMultiblockNode extends AbstractBlockMachine {
    public AbstractBlockMultiblockNode(Material material, Class<? extends AbstractTileMultiblockNode> tileEntityClass) {
        super(material, tileEntityClass);
    }

    /* //no longer used
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if(tileEntity instanceof AbstractTileMultiblockNode) {
            AbstractTileMultiblockNode blockTe = (AbstractTileMultiblockNode) tileEntity;

            TileEntity neighborTe = world.getTileEntity(neighbor);
            if(neighborTe != null) {
                blockTe.validateMultiblockAddition(neighborTe);
            }
        }
    }
    */
}
