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
                else return stackCopy;
            }
            else if(HOTBAR_START_INDEX <= index && index < HOTBAR_END_INDEX) {
                boolean anyMerged = this.mergeItemStack(stack, PLAYER_INVENTORY_START_INDEX, PLAYER_INVENTORY_END_INDEX, false);
                if(!anyMerged) return ItemStack.EMPTY;
                else return stackCopy;
            }
            else { //slot is in the main inventory
                sendDebugMessage("Trying to merge into TileEntity");
                boolean success = false;

                int s = UpgradeSlotWhitelist.findIndexWhereAllowed(stack, tileEntity.getUpgradeSlotWhitelists());
                if(s != -1) {
                    UpgradeSlotWhitelist whitelist = tileEntity.getSlotWhitelist(s);
                    Slot upgradeSlot = this.inventorySlots.get(TILE_ENTITY_START_INDEX + s);
                    ItemStack existingUpgradeStack = upgradeSlot.getStack();

                    //make sure the slot doesn't have any ghost init items in it
                    if(!whitelist.canInsertStack(existingUpgradeStack)) upgradeSlot.putStack(ItemStack.EMPTY);

                    sendDebugMessage(TextFormatting.BLUE + "Trying upgrade slot: " + TextFormatting.RESET + s + " " + TextFormatting.GOLD + whitelist
                            + TextFormatting.LIGHT_PURPLE + " which contains: " + TextFormatting.RESET + existingUpgradeStack);
                    if(whitelist != null && whitelist.canInsertStack(stack)) {
                        sendDebugMessage("Can insert stack");
                        UpgradeSlotWhitelist.UpgradeCardEntry matchingEntry = whitelist.getMatchingUpgradeCardEntry(stack);
                        sendDebugMessage("existing upgrade stack is Empty?: " + existingUpgradeStack.isEmpty());
                        sendDebugMessage("Can stacks stack? : " + ItemHandlerHelper.canItemStacksStack(stack, existingUpgradeStack));

                        if(existingUpgradeStack.isEmpty()) {
                            int numToInsert = MathHelper.clamp(stack.getCount(), 1, matchingEntry.getMaxAmount());
                            ItemStack temp = stack.copy();
                            temp.setCount(numToInsert);
                            sendDebugMessage("slot empty, inserting" + temp);
                            upgradeSlot.putStack(temp);
                            stack.shrink(numToInsert);
                            return stackCopy;
                        }
                        else if(ItemHandlerHelper.canItemStacksStack(stack, existingUpgradeStack)) { //third check for buggy init
                            sendDebugMessage("Can stack stacks.");

                            int numToTransfer = matchingEntry.getMaxAmount() - existingUpgradeStack.getCount();
                            if(numToTransfer > 0) {
                                stack.shrink(numToTransfer);
                                existingUpgradeStack.grow(numToTransfer);
                                return stackCopy; //or return ItemStack.EMPTY ?
                            }
                            else { //Without this clause, excess upgrades are sent into processor on same click
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
                //rest of te slots
                ItemStack remainder = ItemUtils.mergeStackIntoInventory(stack, tileEntity.getItemStackHandler(),
                        ItemUtils.getSlotArrayFromBlackList(tileEntity.getItemStackHandler(), tileEntity.getUpgradeSlotWhitelists().keySet()));
                stack.setCount(remainder.getCount());
                return ItemStack.EMPTY; //stop process no matter what
            }
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}
