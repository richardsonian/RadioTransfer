package com.rlapcs.radiotransfer.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class VecUtils {

    public static EnumFacing getNeighborDirection(BlockPos pos, BlockPos neighbor) {
        Vec3i diff = neighbor.subtract(pos);
        for(EnumFacing facing : EnumFacing.values()) {
            if(facing.getDirectionVec().equals(diff)) {
                return facing;
            }
        }
        return null;
    }
}
