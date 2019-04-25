package com.rlapcs.radiotransfer.machines.deprecated.receiver;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerRadio;
import net.minecraft.inventory.IInventory;

public class ContainerReceiver extends AbstractContainerRadio {
    public ContainerReceiver(IInventory playerInventory, TileReceiver te) {
        super(playerInventory, te);
    }
}
