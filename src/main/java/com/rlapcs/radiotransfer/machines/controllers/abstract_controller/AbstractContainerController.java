package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerWithPlayerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class AbstractContainerController extends AbstractContainerWithPlayerInventory<AbstractTileController> {

    public AbstractContainerController(IInventory playerInventory, AbstractTileController te) {
        super(playerInventory, te);
    }

    @Override
    protected void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 5 + col * 18;
                int y = row * 18 + 71;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 5 + row * 18;
            int y = 127;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Encryption card
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 1, 113, 26));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < tileEntityItemHandlerSlots) {
                if (!this.mergeItemStack(itemstack1, tileEntityItemHandlerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, tileEntityItemHandlerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
