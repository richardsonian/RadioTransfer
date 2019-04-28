package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.sun.org.apache.xpath.internal.operations.Mult;
import net.minecraft.tileentity.TileEntity;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public abstract class AbstractTileMultiblockNode extends AbstractTileMachine {
    public static final int MULTIBLOCK_UPDATE_TICKS = 20;

    protected boolean registeredInMultiblock;
    protected MultiblockRadioController controller;

    public AbstractTileMultiblockNode() {
        registeredInMultiblock = false;
    }

    public void registerInMultiblock(MultiblockRadioController controller) {
        this.controller = controller;
        registeredInMultiblock = true;
        sendDebugMessage(this + " registered to: " + controller);
    }

    public void deregisterFromMultiblock() {
        sendDebugMessage(this + " deregistered from: " + controller);
        if(registeredInMultiblock) {
            controller.removeNode(this);
            this.controller = null;
            registeredInMultiblock = false;
        }
    }
    public boolean isRegisteredInMultiblock() {
        return registeredInMultiblock;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            if(registeredInMultiblock) {
                this.deregisterFromMultiblock();
            }
        }
    }

    public abstract double getPowerUsagePerTick();

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (this.registeredInMultiblock && ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                controller.checkForNewNodes(this.pos);
            }
        }
    }
}
