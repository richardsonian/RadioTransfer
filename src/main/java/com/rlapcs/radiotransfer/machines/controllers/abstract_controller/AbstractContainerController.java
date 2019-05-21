package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerMachine;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class AbstractContainerController extends AbstractContainerMachine<AbstractTileController> {
    protected Coordinate ENCRYPTION_SLOT_POS = new Coordinate(126, 28);

    public AbstractContainerController(IInventory playerInventory, AbstractTileController te) {
        super(playerInventory, te);

        //constant overrides
        PLAYER_INVENTORY_POS = new Coordinate(6, 74);
        HOTBAR_POS = new Coordinate(6, 136);
    }


    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = nextContainerSlotId; //one time for all subclasses
        // Encryption card
        this.addSlotToContainer(new SlotItemHandler(itemHandler, AbstractTileController.ENCRYPTION_CARD_SLOT_INDEX, ENCRYPTION_SLOT_POS.x, ENCRYPTION_SLOT_POS.y));

        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
