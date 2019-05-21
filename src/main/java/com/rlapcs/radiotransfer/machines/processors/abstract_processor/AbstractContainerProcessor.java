package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerMachine;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public abstract class AbstractContainerProcessor extends AbstractContainerMachine<AbstractTileProcessor> {

    public AbstractContainerProcessor(IInventory playerInventory, AbstractTileProcessor te) {
        super(playerInventory, te);

        //constant overrides
        PLAYER_INVENTORY_POS = new Coordinate(6, 112);
        HOTBAR_POS = new Coordinate(6, 174);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}