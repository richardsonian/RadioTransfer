package com.rlapcs.radiotransfer.generic.blocks;

import net.minecraft.util.EnumFacing;

public interface IRadioCableConnectable {
    boolean canConnect(EnumFacing facing);
}
