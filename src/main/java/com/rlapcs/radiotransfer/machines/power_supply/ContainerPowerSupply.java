package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPowerSupply extends AbstractContainerMachine<TilePowerSupply> {
    protected int[] POWER_ITEM_POS = {0, 0};

    public ContainerPowerSupply(IInventory playerInventory, TilePowerSupply te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler tileInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = nextContainerSlotId;
        addSlotToContainer(new SlotItemHandler(tileInventory, TilePowerSupply.POWER_ITEM_INDEX, POWER_ITEM_POS[0], POWER_ITEM_POS[1]));
        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }
}
