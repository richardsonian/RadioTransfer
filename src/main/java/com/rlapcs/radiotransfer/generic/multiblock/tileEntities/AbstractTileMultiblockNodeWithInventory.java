package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachineWithInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractTileMultiblockNodeWithInventory extends AbstractTileMultiblockNode {
    protected ItemStackHandler itemStackHandler;

    public AbstractTileMultiblockNodeWithInventory(int itemStackHandlerSize) {
        super();

        ticksSinceCreation = 0;

        itemStackHandler = new ItemStackHandler(itemStackHandlerSize) {
            @Override
            protected void onContentsChanged(int slot) {
                // We need to tell the tile entity that something has changed so
                // that the chest contents is persisted
                AbstractTileMultiblockNodeWithInventory.this.markDirty();
            }
        };
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

    /**
     * Attempts to merge a stack into an ItemHandler, and returns what was not able to be merged.
     * Returns ItemStack.EMPTY if the merge was 100% successful
     * @param stack
     * @return The remainder of the stack
     */
    public ItemStack mergeStackIntoInventory(ItemStack stack) {
        if(!world.isRemote) {
            if(stack.isEmpty() || stack == null) return ItemStack.EMPTY;

            ItemStack itemstack = stack.copy();
            int slot = 0;
            while(!itemstack.isEmpty() && slot < this.itemStackHandler.getSlots()) {
                itemstack = this.itemStackHandler.insertItem(slot, itemstack, false);
                slot++;
            }

            //No need for block update
            /*
            if(ItemStack.areItemStacksEqual(stack, itemstack)) { //if something was merged into the inventory
                //world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
            }
            */

            return itemstack;
        }

        return null;
    }
}
