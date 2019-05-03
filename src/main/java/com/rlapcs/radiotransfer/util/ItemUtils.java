package com.rlapcs.radiotransfer.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

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
     * @throws IndexOutOfBoundsException if fromSlot is greater than toSlot, or either is out of bounds of the inventory's slot list
     * @throws NullPointerException if passed inventory is null
     */
    public static ItemStack mergeStackIntoInventory(ItemStack stack, IItemHandler inventory, int fromSlot, int toSlot) {
        if(stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        ItemStack itemstack = stack.copy();
        int slot = fromSlot;
        while(!itemstack.isEmpty() && slot < toSlot) {
            itemstack = inventory.insertItem(slot, itemstack, false);
            slot++;
        }

        return itemstack;
    }

    public static boolean isInventoryEmpty(IItemHandler inventory, int fromSlot, int toSlot) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        for(int slot = fromSlot; slot < toSlot; slot++) {
            if(!inventory.getStackInSlot(slot).isEmpty()) return false;
        }

        return true;
    }

    public static ItemStack extractNextItems(IItemHandler inventory, int fromSlot, int toSlot, int amount) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        for(int slot = fromSlot; slot < toSlot; slot++) {
            if(!inventory.getStackInSlot(slot).isEmpty()) return inventory.extractItem(slot, amount, false);
        }
        return ItemStack.EMPTY;
    }

    public static int getFirstIndexInInventoryWhich(IItemHandler inventory, int fromSlot, int toSlot, Predicate<ItemStack> condition) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        for(int s = fromSlot; s < toSlot; s++) {
            if(condition.test(inventory.getStackInSlot(s))) return s;
        }

        return -1;
    }
}
