package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTxController extends AbstractContainerController {
    protected int[] STACK_UPGRADE_SLOT_POS = {106, 48};
    protected int[] SPEED_UPGRADE_SLOT_POS = {126, 48};


    public ContainerTxController(IInventory playerInventory, TileTxController te) {
        super(playerInventory, te);

        initSlots(playerInventory);
    }

    @Override
    protected void addTileEntitySlots() {
        super.addTileEntitySlots();

        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Stack upgrade
        this.addSlotToContainer(new SlotItemHandler(itemHandler, TileTxController.STACK_UPGRADE_SLOT_INDEX, STACK_UPGRADE_SLOT_POS[0], STACK_UPGRADE_SLOT_POS[1]));
        // Speed upgrade
        this.addSlotToContainer(new SlotItemHandler(itemHandler, TileTxController.SPEED_UPGRADE_SLOT_INDEX, SPEED_UPGRADE_SLOT_POS[0], SPEED_UPGRADE_SLOT_POS[1]));

        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }
}
