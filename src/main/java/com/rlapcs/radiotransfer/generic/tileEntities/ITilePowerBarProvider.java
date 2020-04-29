package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.IEnergyStorage;

public interface ITilePowerBarProvider {
    //client methods
    int getDisplayEnergy();
    void setDisplayEnergy(int target);
    double getCachedEnergyUsage();
    double getCachedEnergyGain();
    void setCachedEnergyUsage(double target);
    void setCachedEnergyGain(double target);
    default double getDisplayEnergyAsFraction() {
        return ((double) getDisplayEnergy()) / getEnergyStorage().getMaxEnergyStored();
    }
    default void updateClientVisualPower(int ticksSinceLastUpdate) {
        double effectiveRate = getCachedEnergyGain() - getCachedEnergyUsage();
        setDisplayEnergy(getDisplayEnergy() + (int) Math.round(
                MathHelper.clamp(effectiveRate * ticksSinceLastUpdate,
                        0, getEnergyStorage().getMaxEnergyStored())));
    }
    /**
     * ONLY FOR INTERNAL USE; DO NOT INVOKE; USE CAPABILITY SYSTEM INSTEAD
     * @return the energy capability of the TE
     */
    IEnergyStorage getEnergyStorage();
}
