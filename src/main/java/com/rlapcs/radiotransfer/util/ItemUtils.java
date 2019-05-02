package com.rlapcs.radiotransfer.util;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemUtils {
    /**
     * Attempts to merge a stack into a specified range in an IItemHandler, and returns what was not able to be merged.
     *
     * @param stack The stack to be merged
     * @param inventory The inventory in which the merge will be attempted.
     * @param fromSlot The first slot in the specified range in the inventory, inclusive.
     * @param toSlot The last slot in the specified range in the inventory, exclusive.
     * @return Returns the remainder of what was not able to be inserted into the inventory from the stack.
     *         Returns ItemStack.EMPTY if all of the stack was inserted, or the supplied stack is null or empty.
     * @throws ActionNotSupportedException if fromSlot is greater than toSlot, or either is out of bounds of the inventory's slot list
     */
    public ItemStack mergeStackIntoInventory(ItemStack stack, IItemHandler inventory, int fromSlot, int toSlot) {
        if(stack.isEmpty() || stack == null) return ItemStack.EMPTY;
        if(fromSlot < toSlot || fromSlot < 0 || toSlot >= inventory.getSlots()) throw new ActionNotSupportedException("fromSlot or toSlot nonsensical.");

        ItemStack itemstack = stack.copy();
        int slot = fromSlot;
        while(!itemstack.isEmpty() && slot < toSlot) {
            itemstack = inventory.insertItem(slot, itemstack, false);
            slot++;
        }

        return itemstack;
    }
}
