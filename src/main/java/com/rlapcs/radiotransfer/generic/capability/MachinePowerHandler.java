package com.rlapcs.radiotransfer.generic.capability;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class MachinePowerHandler implements IEnergyStorage, INBTSerializable<NBTTagCompound> {
    //init values
    private AbstractTileMachine owner;
    private int maxEnergy;
    private int maxReceive;
    private int maxExtract;

    //data
    private int energyStored;


    public MachinePowerHandler(int startEnergy, int maxEnergy, int maxReceive, int maxExtract, AbstractTileMachine owner) {
        this.energyStored = MathHelper.clamp(startEnergy, 0, maxEnergy);
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.owner = owner;
    }
    public MachinePowerHandler(int maxEnergy, int maxReceive, int maxExtract, AbstractTileMachine owner) {
        this(0, maxEnergy, maxReceive, maxExtract, owner);
    }
    public MachinePowerHandler(int maxEnergy, int maxTransfer, AbstractTileMachine owner) {
        this(maxEnergy, maxTransfer, maxTransfer, owner);
    }
    public MachinePowerHandler(int maxEnergy, AbstractTileMachine owner) {
        this(maxEnergy, maxEnergy, owner);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("energy", energyStored);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("energy")) {
            energyStored = nbt.getInteger("energy");
        }
    }

    /**
     * Adds energy to the storage. Returns quantity of energy that was accepted.
     *
     * @param maxReceive Maximum amount of energy to be inserted.
     * @param simulate   If TRUE, the insertion will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
     */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }

        int energyReceived = Math.min(maxEnergy - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energyStored += energyReceived;
            onContentsChanged();
        }

        return energyReceived;
    }

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract Maximum amount of energy to be extracted.
     * @param simulate   If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energyStored -= energyExtracted;
            onContentsChanged();
        }

        return energyExtracted;
    }

    /**
     * Returns the amount of energy currently stored.
     */
    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    /**
     * Returns if this storage can have energy extracted.
     * If this is false, then any calls to extractEnergy will return 0.
     */
    @Override
    public boolean canExtract() {
        boolean bandwidth = maxExtract > 0;

        return bandwidth;
    }

    /**
     * Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveEnergy will return 0.
     */
    @Override
    public boolean canReceive() {
        boolean bandwidth = maxReceive > 0;

        return bandwidth;
    }

    protected void onContentsChanged() {}
}
