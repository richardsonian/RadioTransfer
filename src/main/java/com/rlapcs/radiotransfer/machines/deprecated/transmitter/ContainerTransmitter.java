package com.rlapcs.radiotransfer.machines.deprecated.transmitter;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerRadio;
import net.minecraft.inventory.IInventory;

public class ContainerTransmitter extends AbstractContainerRadio {
    public ContainerTransmitter(IInventory playerInventory, TileTransmitter te) {
        super(playerInventory, te);
    }
}