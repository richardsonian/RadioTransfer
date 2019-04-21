package com.rlapcs.radiotransfer.machines.receiver;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerRadio;
import net.minecraft.inventory.IInventory;

public class ContainerReceiver extends AbstractContainerRadio {
    public ContainerReceiver(IInventory playerInventory, TileReceiver te) {
        super(playerInventory, te);
    }
}
