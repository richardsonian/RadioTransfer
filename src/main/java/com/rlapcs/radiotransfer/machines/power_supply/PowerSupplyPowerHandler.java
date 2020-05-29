package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;

/**
 * The Power handler for the PowerSupply
 * Follows Forge Energy Capability behaviour, as RECEIVE ONLY
 * More methods included for internal multiblock use which allow extract + receive etc.
 */
public class PowerSupplyPowerHandler extends MachinePowerHandler {
    public PowerSupplyPowerHandler(TilePowerSupply owner) {
        super(ModConfig.power_options.power_supply.energyCapacity, owner);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~ENERGY CAPABILITY METHODS~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //~~~~~~~~~~~~Disable Extraction~~~~~~~~~~~~~//
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~MULTIBLOCK ACCESS METHODS~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public boolean canUsePower(int energy) {
        //Debug.sendToAllPlayers(String.format("PSUPwrHndlr#canUsePwr() [s:%d, n:%d, %s]", energyStored, energy, (energyStored >= energy)), owner.getWorld());
        return this.energyStored >= energy;
    }
    public int usePower(int energy) {
        int toExtract = Math.min(energyStored, energy);
        energyStored -= toExtract;
        this.onContentsChanged();
        return toExtract;
    }

    public int useAllPower() {
        int used = energyStored;
        energyStored = 0;
        this.onContentsChanged();
        return used;
    }
}
