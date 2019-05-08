package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractContainerProcessor;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractContainerItemProcessor extends AbstractContainerProcessor {
    public static final Item SPEED_UPGRADE_ITEM = Items.APPLE;

    protected static final int NUM_TE_SLOT_ROWS = 4;
    protected static final int NUM_TE_SLOT_COLS = 4;

    protected int[] PROCESSOR_SLOTS_POS;
    protected int[] SPEED_UPGRADE_SLOT_POS;

    public AbstractContainerItemProcessor(IInventory playerInventory, AbstractTileItemProcessor te) {
        super(playerInventory, te);
    }

    @Override
    protected void addTileEntitySlots() {
        IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        TILE_ENTITY_START_INDEX = peekNextSlotId();

        // Speed upgrade
        int index = getNextSlotId();
        this.addSlotToContainer((new SlotItemHandler(itemHandler, index, SPEED_UPGRADE_SLOT_POS[0], SPEED_UPGRADE_SLOT_POS[1])));
        slotBlackList.put(SPEED_UPGRADE_ITEM, index);

        //te inventory slots
        for (int row = 0; row < NUM_TE_SLOT_ROWS; ++row) {
            for (int col = 0; col < NUM_TE_SLOT_COLS; ++col) {
                int x = PROCESSOR_SLOTS_POS[0] + (col * (SLOT_SIZE + SLOT_SPACING));
                int y = PROCESSOR_SLOTS_POS[1] + (row * (SLOT_SIZE + SLOT_SPACING));

                this.addSlotToContainer(new SlotItemHandler(itemHandler, getNextSlotId(), x, y));
            }
        }

        TILE_ENTITY_END_INDEX = peekNextSlotId();
    }
}
