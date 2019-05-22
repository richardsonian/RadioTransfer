package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.ItemStack;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public static final int POWER_USAGE = 10;

    public TileItemEncoder() {
        super();
    }

    @Override
    public boolean canDoProcess() {
        boolean hasItems = !ItemUtils.isInventoryEmpty(itemStackHandler, getNonUpgradeInventorySlots());
        boolean hasSpace = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, getNonUpgradeInventorySlots(), (stack) -> packetQueue.canAddAny(stack)) != -1;
        return hasItems && hasSpace;
    }

    @Override
    public void doProcess() {
        int index = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, getNonUpgradeInventorySlots(), (stack) -> packetQueue.canAddAny(stack));
        if(index != -1) {
            ItemStack stack = itemStackHandler.getStackInSlot(index);
            if (!stack.isEmpty()) {
                ItemStack remainder = packetQueue.add(stack, getItemsPerProcess());
                itemStackHandler.setStackInSlot(index, remainder);
            }
        }
    }

    @Override
    public int getPowerUsagePerTick() {
        return POWER_USAGE;
    }
    @Override
    public ProcessorType getProcessorType() {return ProcessorType.ENCODER;}
}
