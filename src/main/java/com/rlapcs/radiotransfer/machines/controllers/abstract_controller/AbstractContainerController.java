package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerWithPlayerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractContainerController extends AbstractContainerWithPlayerInventory<AbstractTileController> {
    protected int[] ENCRYPTION_SLOT_POS = {126, 28};

    public AbstractContainerController(IInventory playerInventory, AbstractTileController te) {
        super(playerInventory, te);

        //constant overrides
        PLAYER_INVENTORY_POS = new int[] {6, 74};
        HOTBAR_POS = new int[] {6, 136};
    }


    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = nextContainerSlotId; //one time for all subclasses
        sendDebugMessage("Encryption card slot within Container: " + nextContainerSlotId);
        // Encryption card
        this.addSlotToContainer(new SlotItemHandler(itemHandler, AbstractTileController.ENCRYPTION_CARD_SLOT_INDEX, ENCRYPTION_SLOT_POS[0], ENCRYPTION_SLOT_POS[1]));

        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
