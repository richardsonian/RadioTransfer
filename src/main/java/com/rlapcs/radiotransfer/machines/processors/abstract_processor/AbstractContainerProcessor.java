package com.rlapcs.radiotransfer.machines.processors.abstract_processor;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerWithPlayerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class AbstractContainerProcessor extends AbstractContainerWithPlayerInventory<AbstractTileProcessor> {
    public AbstractContainerProcessor(IInventory playerInventory, AbstractTileProcessor te) {
        super(playerInventory, te);
    }

    @Override
    protected void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 6 + col * 20;
                int y = row * 20 + 112;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 6 + row * 20;
            int y = 174;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}