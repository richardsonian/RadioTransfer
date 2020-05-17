package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MAX_FREQUENCY;
import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MIN_FREQUENCY;

public abstract class AbstractTileController extends AbstractTileMultiblockNodeWithInventory {
    //~~~~~~~~~~~~~~~~~~~~~~~~Constants~~~~~~~~~~~~~~~~~~~~~~~~//
    public static final int ENCRYPTION_CARD_SLOT_INDEX = 0;
    protected static final int ABSTRACT_INVENTORY_SIZE = 1;
    //~~~~~~~~~~~~~~~~~~~Instance Variables~~~~~~~~~~~~~~~~~~~~//
    protected boolean activated;
    protected int frequency;
    //used to calculate AbstractTileMultiblockNode#getAverageProcessRate()
    protected int processesCompletedInCycle; //incremented in TileRadio/MultiblockRadioController
    protected double lastAverageProcessRate;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public AbstractTileController(int itemStackHandlerSize) {
        super(itemStackHandlerSize);

        upgradeSlotWhitelists.put(ENCRYPTION_CARD_SLOT_INDEX, ModConstants.UpgradeCards.ENCRYPTION_CARD_WHITELIST);

        frequency = RadioNetwork.MIN_FREQUENCY;
        activated = false;

        processesCompletedInCycle = 0;
        lastAverageProcessRate = 0.0;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~NBT CACHING~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~POWER / PROCESSES~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //Used for power display calculations
    @Override
    public double getAverageProcessesRate() {
        return lastAverageProcessRate;
    }
    protected void incrementProcessesCompletedInCycle() {processesCompletedInCycle++;}
    //Call this to track power usage for controler processes
    public void doProcess() {
        this.incrementProcessesCompletedInCycle();
        this.useProcessPower();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~GETTERS AND SETTERS~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
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

    @Override
    public void update() {
        super.update();
        //~~~~Server Side Updates~~~~//
        if(!world.isRemote) {
            //Refresh average process rate
            if(ticksSinceCreation % AVERAGE_PROCESS_CALC_TICKS == 0) {
                lastAverageProcessRate = (double) processesCompletedInCycle / (double) AVERAGE_PROCESS_CALC_TICKS;
                processesCompletedInCycle = 0;
            }
        }
    }
}
