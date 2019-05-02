package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractContainerProcessor;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public abstract class AbstractContainerItemProcessor extends AbstractContainerProcessor {
    public AbstractContainerItemProcessor(IInventory playerInventory, AbstractTileItemProcessor te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // Speed upgrade
        this.addSlotToContainer((new SlotItemHandler(itemHandler, 1, getUpgradePos()[0], getUpgradePos()[1])));

        // Slots for items
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                int x = getSlotsPos()[0] + col * 20;
                int y = row * 20 + getSlotsPos()[1];
                sendDebugMessage("adding slot from " + itemHandler.getSlots() + " to container for " + te);
                this.addSlotToContainer(new SlotItemHandler(itemHandler, col + row * 4 + 1, x, y));
            }
        }
    }

    protected abstract int[] getUpgradePos();
    protected abstract int[] getSlotsPos();
}
