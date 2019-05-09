package com.rlapcs.radiotransfer.generic.containers;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileItemHandlerProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.Set;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;


public abstract class AbstractContainerMachine<T extends AbstractTileMachine & ITileItemHandlerProvider> extends AbstractContainerWithPlayerInventory<T> {
    public AbstractContainerMachine(IInventory playerInventory, T te) {
        super(playerInventory, te);

        //should call initSlots(playerInventory) in CONCRETE CLASS constructor
    }

    //BROKEN NOT WORKING
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        sendDebugMessage("transferring stack in slot for Container for " + tileEntity);

        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack().copy();

            if (stack.isEmpty()) return ItemStack.EMPTY;

            ItemStack remainder = ItemStack.EMPTY;
            if (TILE_ENTITY_START_INDEX <= index && index < TILE_ENTITY_END_INDEX) {
                boolean allMerged = this.mergeItemStack(stack, HOTBAR_START_INDEX, PLAYER_INVENTORY_END_INDEX, false);
                if(!allMerged) remainder = stack;
            }
            else if(HOTBAR_START_INDEX <= index && index < HOTBAR_END_INDEX) {
                boolean allMerged = this.mergeItemStack(stack, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, false);
                if(!allMerged) remainder = stack;
            }
            else { //slot is in the main inventory
                Set<Integer> applicableSlots = new HashSet<>();
                boolean success = false;
                for(int s : tileEntity.getUpgradeSlotWhitelists().keySet()) {
                    UpgradeSlotWhitelist whitelist = tileEntity.getSlotWhitelist(s);
                    Slot upgradeSlot = this.inventorySlots.get(s);
                    ItemStack existingUpgradeStack = upgradeSlot.getHasStack() ? upgradeSlot.getStack() : ItemStack.EMPTY;
                    if(whitelist != null && whitelist.canInsertStack(stack)) {
                        if(existingUpgradeStack.isEmpty() || ItemHandlerHelper.canItemStacksStack(stack, existingUpgradeStack)) {
                            boolean allMerged = this.mergeItemStack(stack, s, s + 1, false);
                            if(allMerged) {
                                success = true;
                                break;
                            }
                        }
                    }
                }
                if(!success) {
                    remainder = stack;
                }
            }

            return remainder;
        }
        else {
            return super.transferStackInSlot(playerIn, index);
        }
    }
}
