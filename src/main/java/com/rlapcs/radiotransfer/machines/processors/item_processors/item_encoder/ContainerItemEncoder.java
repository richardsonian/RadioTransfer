package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import net.minecraft.inventory.IInventory;

public class ContainerItemEncoder extends AbstractContainerItemProcessor {
    public ContainerItemEncoder(IInventory playerInventory, TileItemEncoder te) {
        super(playerInventory, te);

        //constant overrides
        SPEED_UPGRADE_SLOT_POS = new Coordinate(90, 84);
        PROCESSOR_SLOTS_POS = new Coordinate(6, 24);

        initSlots(playerInventory);
    }
}
