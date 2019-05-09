package com.rlapcs.radiotransfer.generic.tileEntities;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTileMachineWithInventory extends AbstractTileMachine implements ITileItemHandlerProvider {
    protected ItemStackHandler itemStackHandler;
    protected Map<Integer, UpgradeSlotWhitelist> upgradeSlotWhitelists; //slots that should only accept certain items

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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        deserializeInventoryNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        serializeInventoryNBT(compound);
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

    //DO NOT USE THIS GETTER, use capabilities instead
    @Override
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public Map<Integer, UpgradeSlotWhitelist> getUpgradeSlotWhitelists() {
        return upgradeSlotWhitelists;
    }
}
