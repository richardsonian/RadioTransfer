package com.rlapcs.radiotransfer.generic.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractContainerWithPlayerInventory<T extends TileEntity> extends Container {
    //instance variables
    protected int nextContainerSlotId = 0; //keeps track of the next CONTAINER slot index; incremented when you call addSlotToContainer()

    //Constants for slot drawing with some default vals-- override these in the constructor before calling initSlots()
    protected int[] PLAYER_INVENTORY_POS;
    protected int[] HOTBAR_POS;
    protected int SLOT_SPACING = 2;
    protected int SLOT_SIZE = 18;

    //--Index ranges of the slots in the CONTAINER (START INCLUSIVE, END EXCLUSIVE); initialized when slots drawn
    protected int HOTBAR_START_INDEX = -1;
    protected int HOTBAR_END_INDEX = -1;
    protected int PLAYER_INVENTORY_START_INDEX = -1;
    protected int PLAYER_INVENTORY_END_INDEX = -1;
    protected int TILE_ENTITY_START_INDEX = -1;
    protected int TILE_ENTITY_END_INDEX = -1;

    protected T tileEntity;

    public AbstractContainerWithPlayerInventory(IInventory playerInventory, T te) {
        this.tileEntity = te;

        //should call initSlots(playerInventory) in CONCRETE CLASS constructor
    }

    protected void initSlots(IInventory playerInventory) {
        addPlayerSlots(playerInventory);
        addTileEntitySlots();
    }

    protected void addPlayerSlots(IInventory playerInventory) {
        //constants for calculation
        final int numHotbarSlots = 9;
        final int playerInvRows = 3;
        final int playerInvCols = 9;
        final int playerInvSlots = playerInvCols * playerInvRows;

        // Slots for the hotbar; indexes 0-8
        HOTBAR_START_INDEX = nextContainerSlotId;
        //sendDebugMessage("HOTBAR START: " + HOTBAR_START_INDEX);
        for (int col = 0; col < numHotbarSlots; col++) {
            int x = HOTBAR_POS[0] + col * (SLOT_SIZE + SLOT_SPACING);
            int y = HOTBAR_POS[1];
            int index = col; //index within inventory

            this.addSlotToContainer(new Slot(playerInventory, index, x, y));
        }
        HOTBAR_END_INDEX = nextContainerSlotId; //end bound exclusive
        //sendDebugMessage("HOTBAR END: " + HOTBAR_END_INDEX);

        // Slots for the main inventory; 9 - 35
        PLAYER_INVENTORY_START_INDEX = nextContainerSlotId;
        //sendDebugMessage("PLAYER_INV START: " + PLAYER_INVENTORY_START_INDEX);
        for (int row = 0; row < playerInvRows; ++row) {
            for (int col = 0; col < playerInvCols; ++col) {
                int x = PLAYER_INVENTORY_POS[0] + col * (SLOT_SIZE + SLOT_SPACING);
                int y = PLAYER_INVENTORY_POS[1] + row * (SLOT_SIZE + SLOT_SPACING);
                int index = numHotbarSlots + col + (row * playerInvCols);

                this.addSlotToContainer(new Slot(playerInventory, index, x, y));
            }
        }
        PLAYER_INVENTORY_END_INDEX = nextContainerSlotId;
        //sendDebugMessage("PLAYER_INV END: " + PLAYER_INVENTORY_END_INDEX);

    }
    protected abstract void addTileEntitySlots();

    public abstract boolean canInteractWith(EntityPlayer playerIn);

    @Override
    protected Slot addSlotToContainer(Slot slotIn) {
        nextContainerSlotId++;
        return super.addSlotToContainer(slotIn);
    }
}
