package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRxController extends AbstractContainerController {
    protected Coordinate FILTER_SLOT_POS = new Coordinate(126, 48);

    public ContainerRxController(IInventory playerInventory, TileRxController te) {
        super(playerInventory, te);
        initSlots(playerInventory);
    }

    @Override
    protected void addTileEntitySlots() {
        super.addTileEntitySlots();

        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Filter card
        this.addSlotToContainer(new SlotItemHandler(itemHandler, TileRxController.FILTER_SLOT_INDEX, FILTER_SLOT_POS.x, FILTER_SLOT_POS.y));

        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }
}
