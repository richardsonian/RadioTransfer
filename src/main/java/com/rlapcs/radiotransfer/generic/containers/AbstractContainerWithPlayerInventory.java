package com.rlapcs.radiotransfer.generic.containers;

import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Map;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;


public abstract class AbstractContainerWithPlayerInventory<T extends TileEntity> extends Container {
    protected T te;
    protected int tileEntityItemHandlerSlots;
    protected Map<Item, Integer> slotBlackList;

    public AbstractContainerWithPlayerInventory(IInventory playerInventory, T te) {
        this.te = te;
        slotBlackList = new HashMap<>();

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


    //BROKEN
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        sendDebugMessage("transferring stack in slot for Container for " + te);
        if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler teInventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStack stack = super.transferStackInSlot(playerIn, index); //this is weird, but it should work
            if(stack.isEmpty()) return ItemStack.EMPTY;

            ItemStack remainder;
            if(slotBlackList.containsKey(stack.getItem())) {
                //black list
                remainder = teInventory.insertItem(slotBlackList.get(stack.getItem()), stack, false);
            }
            else {
                //other items
                int[] allowedSlots = ItemUtils.getSlotArrayFromBlackList(teInventory, slotBlackList.values());
                remainder = ItemUtils.mergeStackIntoInventory(stack, teInventory, allowedSlots);
            }
            sendDebugMessage("Returning remainder " + remainder);
            return remainder;
        }
        return ItemStack.EMPTY;
    }
    public abstract boolean canInteractWith(EntityPlayer playerIn);

    public Map<Item, Integer> getSlotBlackList() {
        return slotBlackList;
    }
}
