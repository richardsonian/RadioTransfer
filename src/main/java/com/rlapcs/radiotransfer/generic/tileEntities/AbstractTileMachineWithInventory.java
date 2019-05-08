package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public abstract class AbstractTileMachineWithInventory extends AbstractTileMachine {
    protected ItemStackHandler itemStackHandler;
    protected Map<Integer, Set<Item>> upgradeSlotWhitelists; //slots that should only accept one item

    public AbstractTileMachineWithInventory(int itemStackHandlerSize) {
        super();

        ticksSinceCreation = 0;
        upgradeSlotWhitelists = new HashMap<>();

        itemStackHandler = new ItemStackHandler(itemStackHandlerSize) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                AbstractTileMachineWithInventory.this.markDirty();

            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack) && AbstractTileMachineWithInventory.this.isItemValidInSlot(slot, stack);
            }
        };
    }
    protected boolean isItemValidInSlot(int slot, @Nonnull ItemStack stack) {
        Set<Item> whitelist = getSlotWhitelist(slot);
        if(whitelist == null) return true;
        else {
            return whitelist.contains(stack.getItem());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    public Map<Integer, Set<Item>> getUpgradeSlotWhitelists() {
        return upgradeSlotWhitelists;
    }
    public Set<Item> getSlotWhitelist(int slot) {
        if(slot < 0 || slot >= itemStackHandler.getSlots()) throw new IndexOutOfBoundsException("Slot index out of bounds.");
        return upgradeSlotWhitelists.get(slot);
    }
    public int[] getNonUpgradeInventorySlots() {
        return IntStream.range(0, itemStackHandler.getSlots()).filter((s) -> !upgradeSlotWhitelists.keySet().contains(s)).toArray();
    }
}
