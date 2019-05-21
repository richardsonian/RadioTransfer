package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractContainerProcessor;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class AbstractContainerItemProcessor extends AbstractContainerProcessor {
    protected CoordinateXY PROCESSOR_SLOTS_POS;
    protected CoordinateXY SPEED_UPGRADE_SLOT_POS;

    public AbstractContainerItemProcessor(IInventory playerInventory, AbstractTileItemProcessor te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = nextContainerSlotId;

        // Speed upgrade
        this.addSlotToContainer((new SlotItemHandler(itemHandler, AbstractTileItemProcessor.SPEED_UPGRADE_SLOT_INDEX, SPEED_UPGRADE_SLOT_POS.x, SPEED_UPGRADE_SLOT_POS.y)));

        final int teInvRows = 4;
        final int teInvCols = 4;
        final int numUpgradeCards = 1;

        //te inventory slots
        for (int row = 0; row < teInvRows; ++row) {
            for (int col = 0; col < teInvCols; ++col) {
                int x = PROCESSOR_SLOTS_POS.x + (col * (SLOT_SIZE + SLOT_SPACING));
                int y = PROCESSOR_SLOTS_POS.y + (row * (SLOT_SIZE + SLOT_SPACING));
                int index = numUpgradeCards + col + (row * teInvCols);

                this.addSlotToContainer(new SlotItemHandler(itemHandler, index, x, y));
            }
        }

        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }
}
