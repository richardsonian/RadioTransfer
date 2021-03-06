package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import net.minecraft.inventory.IInventory;

public class ContainerItemDecoder extends AbstractContainerItemProcessor {
    public ContainerItemDecoder(IInventory playerInventory, TileItemDecoder te) {
        super(playerInventory, te);

        //constant overrides
        SPEED_UPGRADE_SLOT_POS = new CoordinateXY(83, 84);
        PROCESSOR_SLOTS_POS = new CoordinateXY(106, 24);

        initSlots(playerInventory);
    }

}
