package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MAX_PRIORITY;
import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MIN_PRIORITY;

public class TileRxController extends AbstractTileController {
    private int priority;

    public TileRxController() {
        super();
        priority = MIN_PRIORITY;
    }

    public void changePriority(boolean toIncrement) {
        priority = MathHelper.clamp(getPriority() + (toIncrement ? 1 : -1), MIN_PRIORITY, MAX_PRIORITY);
        this.markDirty();
    }
    public int getPriority() {
        return priority;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("priority")) {
            priority = compound.getInteger("priority");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("priority", priority);

        return compound;
    }
}
