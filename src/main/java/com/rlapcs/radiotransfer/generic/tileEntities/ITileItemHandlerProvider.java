package com.rlapcs.radiotransfer.generic.tileEntities;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.IntStream;

public interface ITileItemHandlerProvider {
    String NBT_TAG_NAME = "items";

    ItemStackHandler getItemStackHandler();
    Map<Integer, UpgradeSlotWhitelist> getUpgradeSlotWhitelists();

    default boolean isItemValidInSlot(int slot, @Nonnull ItemStack stack) {
        UpgradeSlotWhitelist whitelist = getSlotWhitelist(slot);
        if(whitelist == null) return true;
        else {
            return whitelist.canInsertStack(stack);
        }
    }

    default void deserializeInventoryNBT(NBTTagCompound compound) {
        if (compound.hasKey(NBT_TAG_NAME)) {
            getItemStackHandler().deserializeNBT((NBTTagCompound) compound.getTag(NBT_TAG_NAME));
        }
    }
    default NBTTagCompound serializeInventoryNBT(NBTTagCompound compound) {
        compound.setTag(NBT_TAG_NAME, getItemStackHandler().serializeNBT());
        return compound;
    }

    default UpgradeSlotWhitelist getSlotWhitelist(int slot) {
        if(slot < 0 || slot >= getItemStackHandler().getSlots()) throw new IndexOutOfBoundsException("Slot index out of bounds.");
        return getUpgradeSlotWhitelists().get(slot);
    }
    default int[] getNonUpgradeInventorySlots() {
        return IntStream.range(0, getItemStackHandler().getSlots()).filter((s) -> !getUpgradeSlotWhitelists().keySet().contains(s)).toArray();
    }
}
