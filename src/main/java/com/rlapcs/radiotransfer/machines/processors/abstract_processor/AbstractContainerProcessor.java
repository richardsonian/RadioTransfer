package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerWithPlayerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public abstract class AbstractContainerProcessor extends AbstractContainerWithPlayerInventory<AbstractTileProcessor> {

    public AbstractContainerProcessor(IInventory playerInventory, AbstractTileProcessor te) {
        super(playerInventory, te);

        //constant overrides
        PLAYER_INVENTORY_POS = new int[] {6, 112};
        HOTBAR_POS = new int[] {6, 174};
        SLOT_SPACING = 2;
        SLOT_SIZE = 18;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}