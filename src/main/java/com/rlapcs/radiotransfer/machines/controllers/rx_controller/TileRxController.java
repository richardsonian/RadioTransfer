package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.registries.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MAX_PRIORITY;
import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MIN_PRIORITY;

public class TileRxController extends AbstractTileController {
    //~~~~~~~~~~~~~~~~~~~~~Constants~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //Slots and inventory
    public static final int FILTER_SLOT_INDEX = 1;

    public static final int INVENTORY_SIZE = ABSTRACT_INVENTORY_SIZE + 1;

    //~~~~~~~~~~~~~~~~Instance Variables~~~~~~~~~~~~~~~~~~~~~~~~~//
    private int priority;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public TileRxController() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(FILTER_SLOT_INDEX, ModConstants.UpgradeCards.FILTER_CARD_WHITELIST);

        priority = MIN_PRIORITY;
    }

    public void changePriority(boolean toIncrement) {
        priority = MathHelper.clamp(getPriority() + (toIncrement ? 1 : -1), MIN_PRIORITY, MAX_PRIORITY);
        this.markDirty();
    }
    public int getPriority() {
        return priority;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("priority")) {
            priority = compound.getInteger("priority");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("priority", priority);

        return compound;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //isActive() implemented in AbstractTileController

    //~~~~~~~~~~~~~~~~~~~~Base Power Values~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public int getBasePowerPerTick() {
        return ModConfig.power_options.rx_controller.basePowerPerTick;
    }
    @Override
    public int getBasePowerPerProcess() {
        return ModConfig.power_options.rx_controller.basePowerPerTick;
    }

    //~~~~~~~~~~~~~~~~~~~Calculations~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        Map<Item, Integer> out = new HashMap<>();

        out.put(ModItems.encryption_card, itemStackHandler.getStackInSlot(ENCRYPTION_CARD_SLOT_INDEX).getCount());
        out.put(ModItems.filter_card, itemStackHandler.getStackInSlot(FILTER_SLOT_INDEX).getCount());

        return out;
    }

    //Average process rate in AbstractTileController

    //~~~~~~~~~~~~~~~~~~~~~~Upgrade Costs~~~~~~~~~~~~~~~~~~~~~~~~~//
    //Constant
    private static final Map<Item, Integer> upgradeConstantPowerCosts;
    static {
        Map <Item, Integer> tempMap = new HashMap<>();

        tempMap.put(ModItems.encryption_card, ModConfig.power_options.rx_controller.encryptionCardCostPerTick);
        tempMap.put(ModItems.filter_card, ModConfig.power_options.rx_controller.filterCardCostPerTick);

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

        tempMap.put(ModItems.encryption_card, ModConfig.power_options.rx_controller.encryptionCardCostPerProcess);
        tempMap.put(ModItems.filter_card, ModConfig.power_options.rx_controller.filterCardCostPerProcess);

        upgradeProcessPowerCosts = Collections.unmodifiableMap(tempMap);
    }
    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return upgradeProcessPowerCosts;
    }
}
