package com.rlapcs.radiotransfer.generic.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;


public abstract class AbstractContainerWithPlayerInventory<T extends TileEntity> extends Container {
    protected T te;
    protected int tileEntityItemHandlerSlots;

    public AbstractContainerWithPlayerInventory(IInventory playerInventory, T te) {
        this.te = te;

        /*
        if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            this.tileEntityItemHandlerSlots = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots();
        }
        else {
            tileEntityItemHandlerSlots = 0;
        }
        */

        addTileEntitySlots();
        addPlayerSlots(playerInventory);
    }

    protected void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18;
            int y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    protected abstract void addTileEntitySlots();

    public abstract ItemStack transferStackInSlot(EntityPlayer playerIn, int index);
    public abstract boolean canInteractWith(EntityPlayer playerIn);
}
