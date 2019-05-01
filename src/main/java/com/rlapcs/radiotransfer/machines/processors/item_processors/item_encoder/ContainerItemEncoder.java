package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import net.minecraft.inventory.IInventory;

public class ContainerItemEncoder extends AbstractContainerItemProcessor {
    protected int[] upgradePos = {90, 84};
    protected int[] slotsPos = {6, 24};
    protected int[] listPos = {};

    public ContainerItemEncoder(IInventory playerInventory, AbstractTileItemProcessor te) {
        super(playerInventory, te);
    }
}
