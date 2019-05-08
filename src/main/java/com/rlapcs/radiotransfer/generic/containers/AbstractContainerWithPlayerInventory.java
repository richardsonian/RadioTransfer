package com.rlapcs.radiotransfer.generic.containers;

import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Map;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;


public abstract class AbstractContainerWithPlayerInventory<T extends TileEntity> extends Container {
    //instance variables
    protected int nextContainerSlotId = 0;

    //Constants for slot drawing override these in the constructor before calling initSlots()
    protected int[] PLAYER_INVENTORY_POS;
    protected int[] HOTBAR_POS;
    protected int SLOT_SPACING;
    protected int SLOT_SIZE = 18;

    //--Index ranges of the slots in the CONTAINER (START INCLUSIVE, END EXCLUSIVE); initialized when slots drawn
    protected int HOTBAR_START_INDEX = -1;
    protected int HOTBAR_END_INDEX = -1;
    protected int PLAYER_INVENTORY_START_INDEX = -1;
    protected int PLAYER_INVENTORY_END_INDEX = -1;
    protected int TILE_ENTITY_START_INDEX = -1;
    protected int TILE_ENTITY_END_INDEX = -1;

    protected T tileEntity;
    protected Map<Item, Integer> slotBlackList; //slots that should only accept one item

    public AbstractContainerWithPlayerInventory(IInventory playerInventory, T te) {
        this.tileEntity = te;
        slotBlackList = new HashMap<>();

        //should call initSlots(playerInventory) in CONCRETE CLASS constructor
    }

    protected void initSlots(IInventory playerInventory) {
        addPlayerSlots(playerInventory);
        addTileEntitySlots();
    }

    protected void addPlayerSlots(IInventory playerInventory) {
        // Slots for the hotbar; indexes 0-8
        HOTBAR_START_INDEX = nextContainerSlotId;
        for (int col = 0; col < 9; col++) {
            int x = HOTBAR_POS[0] + col * (SLOT_SIZE + SLOT_SPACING);
            int y = HOTBAR_POS[1];
            int index = col; //index within inventory

            this.addSlotToContainer(new Slot(playerInventory, index, x, y));
            nextContainerSlotId++;
        }
        HOTBAR_END_INDEX = nextContainerSlotId; //end bound exclusive

        // Slots for the main inventory; 9 - 35
        PLAYER_INVENTORY_START_INDEX = nextContainerSlotId;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = PLAYER_INVENTORY_POS[0] + col * (SLOT_SIZE + SLOT_SPACING);
                int y = PLAYER_INVENTORY_POS[1] + row * (SLOT_SIZE + SLOT_SPACING);
                int index = 9 + col + (row * 9); //is this right
                this.addSlotToContainer(new Slot(playerInventory, index, x, y));
                nextContainerSlotId++;
            }
        }
        PLAYER_INVENTORY_END_INDEX = nextContainerSlotId;

    }
    protected abstract void addTileEntitySlots();


    //BROKEN NOT WORKING
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        sendDebugMessage("transferring stack in slot for Container for " + tileEntity);
        if(tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler teInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemStack stack = super.transferStackInSlot(playerIn, index); //this is weird, but it should work
            if(stack.isEmpty()) return ItemStack.EMPTY;

            ItemStack remainder;
            if(slotBlackList.containsKey(stack.getItem())) {
                //black list
                remainder = teInventory.insertItem(slotBlackList.get(stack.getItem()), stack, false);
            }
            else {
                //other items
                int[] allowedSlots = ItemUtils.getSlotArrayFromBlackList(teInventory, slotBlackList.values());
                remainder = ItemUtils.mergeStackIntoInventory(stack, teInventory, allowedSlots);
            }
            sendDebugMessage("Returning remainder " + remainder);
            return remainder;
        }
        return ItemStack.EMPTY;
    }

    public abstract boolean canInteractWith(EntityPlayer playerIn);

    public Map<Item, Integer> getSlotBlackList() {
        return slotBlackList;
    }
}
