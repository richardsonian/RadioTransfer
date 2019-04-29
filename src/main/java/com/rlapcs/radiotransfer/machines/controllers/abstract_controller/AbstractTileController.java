package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractTileController extends AbstractTileMultiblockNodeWithInventory {
    public static final int UPPER_FREQUENCY_LIMIT = 5;
    public static final int LOWER_FREQUENCY_LIMIT = 1;

    protected boolean registered;
    protected boolean activated;
    protected int frequency;

    protected static final int INVENTORY_SIZE = 12;
    protected static final double BASE_POWER_USAGE = 10;

    public AbstractTileController() {
        super(INVENTORY_SIZE);
        registered = false;
        activated = true;
        frequency = 1;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public double getPowerUsagePerTick() {
        return BASE_POWER_USAGE;
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

    /* Attribute getters and setters */
    public void setActivated(boolean target) {
        activated = target;
        this.markDirty();
    }
    public boolean getActivated() {return activated;}

    public void changeFrequency(boolean toIncrement) {
        int newFrequency = MathHelper.clamp(getFrequency() + (toIncrement ? 1 : -1), LOWER_FREQUENCY_LIMIT, UPPER_FREQUENCY_LIMIT);

        setFrequency(newFrequency);
    }

    private void setFrequency(int target) {
        frequency = target;
        this.markDirty();
    }
}
