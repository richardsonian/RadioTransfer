package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileMultiblockNode extends AbstractTileMachine {

    protected TileRadio controller;

    public AbstractTileMultiblockNode() {}

    public abstract double getPowerUsagePerTick();

    public abstract void triggerControllerUpdate();

    public void setController(TileRadio controller) {
        this.controller = controller;
    }

    public TileRadio getController() {
        return controller;
    }

    public void validateMultiblockAddition(TileEntity newTe) {

    }

}
