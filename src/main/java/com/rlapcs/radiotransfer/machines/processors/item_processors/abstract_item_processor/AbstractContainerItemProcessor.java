package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractContainerProcessor;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public abstract class AbstractContainerItemProcessor extends AbstractContainerProcessor {
    public static final int SPEED_UPGRADE_SLOT_INDEX = 0; //changed this to 0
    public static final int TILE_SLOTS_START_INDEX = 1;

    public AbstractContainerItemProcessor(IInventory playerInventory, AbstractTileItemProcessor te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // Speed upgrade
        this.addSlotToContainer((new SlotItemHandler(itemHandler, SPEED_UPGRADE_SLOT_INDEX, getUpgradePos()[0], getUpgradePos()[1])));

        final int numRows = 4;
        final int numCols = 4;
        final int slotSize = 20;

        // Slots for items
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                int x = getSlotsPos()[0] + (col * slotSize);
                int y = getSlotsPos()[1] + (row * slotSize);
                int index = col + (row * 4) + TILE_SLOTS_START_INDEX;

                this.addSlotToContainer(new SlotItemHandler(itemHandler, index, x, y));
            }
        }
    }

    protected abstract int[] getUpgradePos();
    protected abstract int[] getSlotsPos();
}