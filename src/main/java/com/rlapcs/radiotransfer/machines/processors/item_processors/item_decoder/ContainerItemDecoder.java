package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import net.minecraft.inventory.IInventory;

public class ContainerItemDecoder extends AbstractContainerItemProcessor {
    public ContainerItemDecoder(IInventory playerInventory, TileItemDecoder te) {
        super(playerInventory, te);

        //constant overrides
        SPEED_UPGRADE_SLOT_POS = new int[] {83, 84};
        PROCESSOR_SLOTS_POS = new int[] {106, 24}; //needs to be changed

        initSlots(playerInventory);
    }

}
