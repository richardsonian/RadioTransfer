package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public static final int POWER_USAGE = 10;

    public TileItemEncoder() {
        super();
    }

    @Override
    public boolean canDoProcess() {
        boolean superCheck = super.canDoProcess();
        boolean hasItems = !ItemUtils.isInventoryEmpty(itemStackHandler, getNonUpgradeInventorySlots());
        boolean hasSpace = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, getNonUpgradeInventorySlots(), (stack) -> packetQueue.canAddAny(stack)) != -1;
        return superCheck && hasItems && hasSpace;
    }

    @Override
    public void doProcess() {
        //super.doProcess();
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

    //Power
    @Override
    public int getBasePowerPerTick() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardConstantPowerCosts() {
        return null;
    }

    @Override
    public int getBasePowerPerProcess() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return null;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        return null;
    }

    @Override
    public int getAverageProcessesRate() {
        return 0;
    }
}
