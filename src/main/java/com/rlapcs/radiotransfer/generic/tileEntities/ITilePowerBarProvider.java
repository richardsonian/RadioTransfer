package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.IEnergyStorage;

public interface ITilePowerBarProvider {
    //client methods
    int getDisplayEnergy();
    void setDisplayEnergy(int target);
    int getMaxEngergy();
}
