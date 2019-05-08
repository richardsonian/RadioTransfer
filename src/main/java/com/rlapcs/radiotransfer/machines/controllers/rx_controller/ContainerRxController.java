package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerRxController extends AbstractContainerController {
    /* RX CONTROLLER MAY NOT HAVE SPEED UPGRADE
    public static final Item SPEED_UPGRADE_ITEM = Items.STICK; //placeholder
    protected int[] SPEED_UPGRADE_SLOT_POS = {126, 48};
    */

    public ContainerRxController(IInventory playerInventory, TileRxController te) {
        super(playerInventory, te);
        initSlots(playerInventory);
    }

    @Override
    protected void addTileEntitySlots() {
        super.addTileEntitySlots();

        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        /*
        // Speed upgrade
        int index = getNextSlotId();
        this.addSlotToContainer(new SlotItemHandler(itemHandler, index, SPEED_UPGRADE_SLOT_POS[0], SPEED_UPGRADE_SLOT_POS[1]));
        slotBlackList.put(SPEED_UPGRADE_ITEM, index);

        TILE_ENTITY_END_INDEX = peekNextSlotId();
        */
    }
}
