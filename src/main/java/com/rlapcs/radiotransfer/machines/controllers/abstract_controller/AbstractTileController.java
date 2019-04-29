package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MAX_FREQUENCY;
import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MIN_FREQUENCY;

public abstract class AbstractTileController extends AbstractTileMultiblockNodeWithInventory {
    protected static final int INVENTORY_SIZE = 12;
    protected static final double POWER_USAGE = 10;

    private int frequency;
    private boolean activated;


    public AbstractTileController() {
        super(INVENTORY_SIZE);

        frequency = MIN_FREQUENCY;
        activated = false;
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }

    public void setActivated(boolean target) {
        activated = target;
        this.markDirty();
    }
    public boolean getActivated() {return activated;}

    public void changeFrequency(boolean toIncrement) {
        int newFrequency = MathHelper.clamp(getFrequency() + (toIncrement ? 1 : -1), MIN_FREQUENCY, MAX_FREQUENCY);

        setFrequency(newFrequency);
    }
    private void setFrequency(int target) {
        frequency = target;
        this.markDirty();
    }
    public int getFrequency() {
        return frequency;
    }

    /* NBT data */
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("activated")) {
            this.activated = compound.getBoolean("activated");
        }
        if(compound.hasKey("frequency")) {
            this.frequency = compound.getInteger("frequency");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("activated", this.activated);
        compound.setInteger("frequency", this.frequency);

        return compound;
    }
}
