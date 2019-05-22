package com.rlapcs.radiotransfer.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ItemUtils {
    public static int[] getSlotArrayFromBlackList(IItemHandler inventory, Collection<Integer> blackList) {
        return IntStream.range(0, inventory.getSlots()).filter((i) -> !blackList.contains(i)).toArray();
    }

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
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");
        return mergeStackIntoInventory(stack, inventory, IntStream.range(fromSlot, toSlot).toArray());
    }
    public static ItemStack mergeStackIntoInventory(ItemStack stack, IItemHandler inventory, int[] allowedSlots) {
        if(stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");

        if(allowedSlots == null) allowedSlots = IntStream.range(0, inventory.getSlots()).toArray();

        ItemStack itemstack = stack.copy();
        for(int slot : allowedSlots) {
            if(slot < 0 || slot >= inventory.getSlots()) throw new IndexOutOfBoundsException("Slot " + slot + " in allowedSlots out of bounds");
            if(inventory.isItemValid(slot, itemstack)) {
                itemstack = inventory.insertItem(slot, itemstack, false);
                if (itemstack.isEmpty()) return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }

    public static boolean canMergeAnyIntoInventory(ItemStack stack, IItemHandler inventory, int fromSlot, int toSlot) {
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        return canMergeAnyIntoInventory(stack, inventory, IntStream.range(fromSlot, toSlot).toArray());
    }
    public static boolean canMergeAnyIntoInventory(ItemStack stack, IItemHandler inventory, int[] allowedSlots) {
        if(stack == null || stack.isEmpty()) return false;
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(allowedSlots == null) allowedSlots = IntStream.range(0, inventory.getSlots()).toArray();

        for(int slot : allowedSlots) {
            if(slot < 0 || slot >= inventory.getSlots()) throw new IndexOutOfBoundsException("Slot " + slot + " in allowedSlots out of bounds");
            ItemStack inventoryStack = inventory.getStackInSlot(slot);

            if(inventoryStack.isEmpty()) return true;
            else if(ItemHandlerHelper.canItemStacksStack(inventoryStack, stack) && inventory.isItemValid(slot, stack)) {
                if(inventoryStack.getCount() < inventory.getSlotLimit(slot) && inventoryStack.getCount() < inventoryStack.getMaxStackSize()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isInventoryEmpty(IItemHandler inventory, int fromSlot, int toSlot) {
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        return isInventoryEmpty(inventory, IntStream.range(fromSlot, toSlot).toArray());
    }
    public static boolean isInventoryEmpty(IItemHandler inventory, int[] slotsToCheck) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(slotsToCheck == null) slotsToCheck = IntStream.range(0, inventory.getSlots()).toArray();

        for(int slot : slotsToCheck) {
            if(!inventory.getStackInSlot(slot).isEmpty()) return false;
        }

        return true;
    }


    public static ItemStack extractNextItems(IItemHandler inventory, int fromSlot, int toSlot, int amount) {
        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        return extractNextItems(inventory, IntStream.range(fromSlot, toSlot).toArray(), amount);
    }
    public static ItemStack extractNextItems(IItemHandler inventory, int[] allowedSlots, int amount) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(amount <= 0) throw new InvalidParameterException("amount must be greater than 0");
        if(allowedSlots == null) allowedSlots = IntStream.range(0, inventory.getSlots()).toArray();

        for(int slot : allowedSlots) {
            if(!inventory.getStackInSlot(slot).isEmpty()) return inventory.extractItem(slot, amount, false);
        }
        return ItemStack.EMPTY;
    }

    public static int getFirstIndexInInventoryWhich(IItemHandler inventory, int fromSlot, int toSlot, Predicate<ItemStack> condition) {

        if(fromSlot > toSlot || fromSlot < 0 || toSlot > inventory.getSlots()) throw new IndexOutOfBoundsException("fromSlot or toSlot nonsensical.");

        return getFirstIndexInInventoryWhich(inventory, IntStream.range(fromSlot, toSlot).toArray(), condition);
    }
    public static int getFirstIndexInInventoryWhich(IItemHandler inventory, int[] slotsToCheck, Predicate<ItemStack> condition) {
        if(inventory == null) throw new NullPointerException("IItemHandler passed is null");
        if(slotsToCheck == null) slotsToCheck = IntStream.range(0, inventory.getSlots()).toArray();

        for(int slot : slotsToCheck) {
            if(condition.test(inventory.getStackInSlot(slot))) return slot;
        }

        return -1;
    }
}
