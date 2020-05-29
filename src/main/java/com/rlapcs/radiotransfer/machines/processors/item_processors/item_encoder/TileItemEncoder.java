package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.registries.ModItems;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public TileItemEncoder() {
        super();
    }

    @Override
    public ProcessorType getProcessorType() {return ProcessorType.ENCODER;}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~Process Logic~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    private boolean hasItemsAndSpace() { //Server and client, as packet queue is sent to clients with network messages
        boolean hasItems = !ItemUtils.isInventoryEmpty(itemStackHandler, getNonUpgradeInventorySlots());
        boolean hasSpace = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, getNonUpgradeInventorySlots(), (stack) -> packetQueue.canAddAny(stack)) != -1;
        return hasItems && hasSpace;
    }

    @Override
    public boolean canDoProcessServer() {
        boolean superCheck = super.canDoProcessServer();
        boolean hasItemsAndSpace = hasItemsAndSpace();
        return superCheck && hasItemsAndSpace;
    }
    @Override
    public boolean canDoProcessClient() {
        boolean superCheck = super.canDoProcessClient();
        boolean hasItemsAndSpace = hasItemsAndSpace();
        return superCheck && hasItemsAndSpace;
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
            super.doProcess(); //use power and mark process
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //isActive() implemented in AbstractTileMaterialProcessor

    //~~~~~~~~~~~~~~~~~~~~Base Power Values~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public int getBasePowerPerTick() {
        return ModConfig.power_options.item_encoder.basePowerPerTick;
    }
    @Override
    public int getBasePowerPerProcess() {
        return ModConfig.power_options.item_encoder.basePowerPerTick;
    }

    //~~~~~~~~~~~~~~~~~~~Calculations~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        Map<Item, Integer> out = new HashMap<>();
        out.put(ModItems.speed_upgrade, itemStackHandler.getStackInSlot(SPEED_UPGRADE_SLOT_INDEX).getCount());
        return out;
    }

    //Average process rate calculated in AbstractTileMaterialProcessor
    
    //~~~~~~~~~~~~~~~~~~~~~~Upgrade Costs~~~~~~~~~~~~~~~~~~~~~~~~~//
    //Constant
    private static final Map<Item, Integer> upgradeConstantPowerCosts;
    static {
        Map <Item, Integer> tempMap = new HashMap<>();
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.item_encoder.speedUpgradeCostPerTick);
        upgradeConstantPowerCosts = Collections.unmodifiableMap(tempMap);
    }
    @Override
    public Map<Item, Integer> getUpgradeCardConstantPowerCosts() {
        return upgradeConstantPowerCosts;
    }

    //Process
    private static final Map<Item, Integer> upgradeProcessPowerCosts;
    static {
        Map <Item, Integer> tempMap = new HashMap<>();
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.item_encoder.speedUpgradeCostPerProcess);
        upgradeProcessPowerCosts = Collections.unmodifiableMap(tempMap);
    }
    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return upgradeProcessPowerCosts;
    }
}
