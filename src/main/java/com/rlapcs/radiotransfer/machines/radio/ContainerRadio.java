package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerMachine;
import net.minecraft.inventory.IInventory;

public class ContainerRadio extends AbstractContainerMachine<TileRadio> {
    public ContainerRadio(IInventory playerInventory, TileRadio te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {

    }
}
