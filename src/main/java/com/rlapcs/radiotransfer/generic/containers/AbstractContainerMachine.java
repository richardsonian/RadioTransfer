package com.rlapcs.radiotransfer.generic.containers;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileItemHandlerProvider;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;


public abstract class AbstractContainerMachine<T extends AbstractTileMachine & ITileItemHandlerProvider> extends AbstractContainerWithPlayerInventory<T> {
    public AbstractContainerMachine(IInventory playerInventory, T te) {
        super(playerInventory, te);

        //should call initSlots(playerInventory) in CONCRETE CLASS constructor
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        sendDebugMessage("transferring stack in slot for Container for " + tileEntity);

        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();

            if (stack.isEmpty()) return ItemStack.EMPTY;

            ItemStack stackCopy = stack.copy();
            if (TILE_ENTITY_START_INDEX <= index && index < TILE_ENTITY_END_INDEX) {
                boolean anyMerged = this.mergeItemStack(stack, HOTBAR_START_INDEX, PLAYER_INVENTORY_END_INDEX, false);
                if(!anyMerged) return ItemStack.EMPTY;
            }
            else if(HOTBAR_START_INDEX <= index && index < HOTBAR_END_INDEX) {
                boolean anyMerged = this.mergeItemStack(stack, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, false);
                if(!anyMerged) return ItemStack.EMPTY;
            }
            else { //slot is in the main inventory
                sendDebugMessage("Trying to merge into TileEntity");
                boolean success = false;
                Collection<Integer> upgradeSlotIndexes = tileEntity.getUpgradeSlotWhitelists().keySet();
                for(int s : upgradeSlotIndexes) {
                    UpgradeSlotWhitelist whitelist = tileEntity.getSlotWhitelist(s);
                    Slot upgradeSlot = this.inventorySlots.get(s);
                    ItemStack existingUpgradeStack = upgradeSlot.getStack();
                    sendDebugMessage(TextFormatting.BLUE + "Trying upgrade slot " + TextFormatting.RESET + s + TextFormatting.GOLD + whitelist
                            + TextFormatting.LIGHT_PURPLE + " which contains: " + TextFormatting.RESET + existingUpgradeStack);
                    if(whitelist != null && whitelist.canInsertStack(stack)) {
                        sendDebugMessage("Can insert stack");
                        UpgradeSlotWhitelist.UpgradeCardEntry matchingEntry = whitelist.getMatchingUpgradeCardEntry(stack);
                        if(existingUpgradeStack.isEmpty() || ItemHandlerHelper.canItemStacksStack(stack, existingUpgradeStack)) { //and check max quantity
                            sendDebugMessage("Can stack stacks.");
                            int maxQuantity = MathHelper.clamp(matchingEntry.getMaxAmount(), 1, stack.getMaxStackSize());
                            int numToTransfer = maxQuantity - existingUpgradeStack.getCount();
                            stack.shrink(numToTransfer);
                            existingUpgradeStack.grow(numToTransfer); //not sure if this object reference is connected to inventory
                            if(numToTransfer > 0) {
                                sendDebugMessage("Stacked (at least) some of stacks!");
                                success = true;
                            }
                        }
                    }
                }
                if(!success) {
                    ItemStack remainder = ItemUtils.mergeStackIntoInventory(stack, tileEntity.getItemStackHandler(),
                            ItemUtils.getSlotArrayFromBlackList(tileEntity.getItemStackHandler(), tileEntity.getUpgradeSlotWhitelists().keySet()));
                    if(ItemStack.areItemStacksEqual(stack, remainder)) {
                        return ItemStack.EMPTY;
                    }
                    else {
                        stack = remainder;
                    }
                }
            }
            return stackCopy;
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}
