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
    private boolean hasPacketsAndSpace() { //Server and client, as packet queue is sent to clients with network messages
        boolean hasPackets = !packetQueue.isEmpty();
        boolean hasSpace = ItemUtils.canMergeAnyIntoInventory(packetQueue.peekNextPacket(ItemPacketQueue.MAX_QUANTITY), itemStackHandler, getNonUpgradeInventorySlots());
        return hasPackets && hasSpace;
    }

    @Override
    public boolean canDoProcessServer() {
        boolean superCheck = super.canDoProcessServer();
        boolean hasPacketsAndSpace = hasPacketsAndSpace();
        return superCheck && hasPacketsAndSpace;
    }

    @Override
    public boolean canDoProcessClient() {
        boolean superCheck = super.canDoProcessClient();
        boolean hasPacketsAndSpace = hasPacketsAndSpace();
        return superCheck && hasPacketsAndSpace;
    }
    @Override
    public void doProcess() {
        ItemStack toProcess = packetQueue.getNextPacket(getItemsPerProcess());
        ItemStack remainder = ItemUtils.mergeStackIntoInventory(toProcess, itemStackHandler, getNonUpgradeInventorySlots());
        packetQueue.add(remainder);
        if(toProcess != null && !toProcess.isEmpty() && !ItemStack.areItemStacksEqual(toProcess, remainder))
            super.doProcess(); //Mark process and use power
        //Debug.sendToAllPlayers("Processing packet " + toProcess + " into items in " + this, world);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //isActive() implemented in AbstractTileMaterialProcessor

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