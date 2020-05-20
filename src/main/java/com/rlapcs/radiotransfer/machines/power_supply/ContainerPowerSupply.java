package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.generic.containers.AbstractContainerMachine;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPowerSupply extends AbstractContainerMachine<TilePowerSupply> {
    protected CoordinateXY POWER_ITEM_POS = new CoordinateXY(141, 83);

    public ContainerPowerSupply(IInventory playerInventory, TilePowerSupply te) {
        super(playerInventory, te);

        //constant overrides
        PLAYER_INVENTORY_POS = new CoordinateXY(6, 112);
        HOTBAR_POS = new CoordinateXY(6, 174);

        initSlots(playerInventory);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler tileInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = nextContainerSlotId;
        addSlotToContainer(new SlotItemHandler(tileInventory, TilePowerSupply.POWER_ITEM_INDEX, POWER_ITEM_POS.x, POWER_ITEM_POS.y));
        TILE_ENTITY_END_INDEX = nextContainerSlotId;
    }
}
