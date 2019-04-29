package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;

public abstract class AbstractTileController extends AbstractTileMultiblockNodeWithInventory {
    protected static final int INVENTORY_SIZE = 12;
    protected static final double POWER_USAGE = 10;

    public AbstractTileController() {
        super(INVENTORY_SIZE);
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }
}
