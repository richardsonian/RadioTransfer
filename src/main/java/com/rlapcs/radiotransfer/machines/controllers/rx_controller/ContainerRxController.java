package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRxController extends AbstractContainerController {
    public ContainerRxController(IInventory playerInventory, TileRxController te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        super.addTileEntitySlots();

        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Speed upgrade
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 2, 113, 48));
    }
}
