package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTxController extends AbstractContainerController {
    public ContainerTxController(IInventory playerInventory, TileTxController te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        super.addTileEntitySlots();

        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Stack upgrade
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 2, 95, 48));

        // Speed upgrade
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 3, 113, 48));
    }
}
