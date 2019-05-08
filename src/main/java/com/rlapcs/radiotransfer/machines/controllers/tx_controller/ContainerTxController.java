package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractContainerController;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTxController extends AbstractContainerController {
    public static final Item STACK_UPGRADE_ITEM = Items.ARROW; //placeholders
    protected int[] STACK_UPGRADE_SLOT_POS = {106, 48};
    public static final Item SPEED_UPGRADE_ITEM = Items.STICK; //placeholder
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
        int index = getNextSlotId();
        this.addSlotToContainer(new SlotItemHandler(itemHandler, index, STACK_UPGRADE_SLOT_POS[0], STACK_UPGRADE_SLOT_POS[1]));
        slotBlackList.put(STACK_UPGRADE_ITEM, index);

        // Speed upgrade
        int index1 = getNextSlotId();
        this.addSlotToContainer(new SlotItemHandler(itemHandler, index1, SPEED_UPGRADE_SLOT_POS[0], SPEED_UPGRADE_SLOT_POS[1]));
        slotBlackList.put(SPEED_UPGRADE_ITEM, index1);

        TILE_ENTITY_END_INDEX = peekNextSlotId();
    }
}
