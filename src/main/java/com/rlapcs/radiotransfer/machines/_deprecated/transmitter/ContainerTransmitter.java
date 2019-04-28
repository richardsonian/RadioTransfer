package com.rlapcs.radiotransfer.machines._deprecated.transmitter;

import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractContainerRadio;
import net.minecraft.inventory.IInventory;

public class ContainerTransmitter extends AbstractContainerRadio {
    public ContainerTransmitter(IInventory playerInventory, TileTransmitter te) {
        super(playerInventory, te);
    }
}