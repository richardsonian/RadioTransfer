package com.rlapcs.radiotransfer.machines._deprecated.receiver;

import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractContainerRadio;
import net.minecraft.inventory.IInventory;

public class ContainerReceiver extends AbstractContainerRadio {
    public ContainerReceiver(IInventory playerInventory, TileReceiver te) {
        super(playerInventory, te);
    }
}
