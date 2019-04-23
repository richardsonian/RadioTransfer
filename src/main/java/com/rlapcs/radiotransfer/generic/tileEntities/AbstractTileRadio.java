package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class AbstractTileRadio extends AbstractTileMachine {
    public static final int UPPER_FREQUENCY_LIMIT = 5;
    public static final int LOWER_FREQUENCY_LIMIT = 1;

    public static final int ITEM_STACK_HANDLER_SIZE = 12;

    protected boolean registered;
    protected boolean activated;
    protected int frequency;

    public AbstractTileRadio() {
        super(ITEM_STACK_HANDLER_SIZE);
        registered = false;
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

    public void changeFrequency(boolean toIncrement) {
        int newFrequency = MathHelper.clamp(getFrequency() + (toIncrement ? 1 : -1), LOWER_FREQUENCY_LIMIT, UPPER_FREQUENCY_LIMIT);

        setFrequency(newFrequency);
    }

    private void setFrequency(int target) {
        frequency = target;
        this.markDirty();
    }

    public int getFrequency() {
        return frequency;
    }
}
