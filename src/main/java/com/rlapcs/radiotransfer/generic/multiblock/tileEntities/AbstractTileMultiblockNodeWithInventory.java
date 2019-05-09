package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileItemHandlerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class
AbstractTileMultiblockNodeWithInventory extends AbstractTileMultiblockNode implements ITileItemHandlerProvider {
    protected ItemStackHandler itemStackHandler;
    protected Map<Integer, UpgradeSlotWhitelist> upgradeSlotWhitelists; //slots that should only accept certain items

    public AbstractTileMultiblockNodeWithInventory(int itemStackHandlerSize) {
        super();

        ticksSinceCreation = 0;
        upgradeSlotWhitelists = new HashMap<>();

        itemStackHandler = new ItemStackHandler(itemStackHandlerSize) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                AbstractTileMultiblockNodeWithInventory.this.markDirty();
            }
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                boolean superPassed = super.isItemValid(slot, stack);
                return superPassed && AbstractTileMultiblockNodeWithInventory.this.isItemValidInSlot(slot, stack);
            }
            @Override
            public int getStackLimit(int slot, ItemStack stack) {
                UpgradeSlotWhitelist wl = upgradeSlotWhitelists.get(slot);
                if(wl != null && wl.canInsertStack(stack)) {
                    return Math.min(wl.getMatchingUpgradeCardEntry(stack).getMaxAmount(), super.getStackLimit(slot, stack));
                }
                else {
                    return super.getStackLimit(slot, stack);
                }
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
