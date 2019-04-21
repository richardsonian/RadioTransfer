package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public abstract class AbstractTileRadio extends AbstractTileMachine implements ITickable {
    public static final int ITEM_STACK_HANDLER_SIZE = 12;

    protected boolean activated;
    protected int frequency;

    public AbstractTileRadio() {
        super(ITEM_STACK_HANDLER_SIZE);
        activated = true;
        frequency = 1;
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

    public void setFrequency(int target) {
        frequency = target;
        this.markDirty();
    }
    public int getFrequency() {
        return frequency;
    }


    @Override
    public void update() {

    }
}
