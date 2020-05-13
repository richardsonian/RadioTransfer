package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.registries.ModItems;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TileItemDecoder extends AbstractTileItemProcessor {
    public TileItemDecoder() {
        super();
    }
    @Override
    public ProcessorType getProcessorType() {return ProcessorType.DECODER;}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~Process Logic~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public boolean canDoProcess() {
        boolean superCheck = super.canDoProcess();
        boolean hasPackets = !packetQueue.isEmpty();
        boolean hasSpace = ItemUtils.canMergeAnyIntoInventory(packetQueue.peekNextPacket(ItemPacketQueue.MAX_QUANTITY), itemStackHandler, getNonUpgradeInventorySlots());

        //Debug.sendToAllPlayers(String.format("Decoder hasPackets(%b), hasSpace(%b)", hasPackets, hasSpace), world);
        return superCheck && hasPackets && hasSpace;
    }
    @Override
    public void doProcess() {
        //super.doProcess();
        ItemStack toProcess = packetQueue.getNextPacket(getItemsPerProcess());
        ItemStack remainder = ItemUtils.mergeStackIntoInventory(toProcess, itemStackHandler, getNonUpgradeInventorySlots());
        packetQueue.add(remainder);
        //Debug.sendToAllPlayers("Processing packet " + toProcess + " into items in " + this, world);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //~~~~~~~~~~~~~~~~~~~~Base Power Values~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public int getBasePowerPerTick() {
        return ModConfig.power_options.item_decoder.basePowerPerTick;
    }
    @Override
    public int getBasePowerPerProcess() {
        return ModConfig.power_options.item_decoder.basePowerPerTick;
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
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.item_decoder.speedUpgradeCostPerTick);
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
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.item_decoder.speedUpgradeCostPerProcess);
        upgradeProcessPowerCosts = Collections.unmodifiableMap(tempMap);
    }
    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return upgradeProcessPowerCosts;
    }
}