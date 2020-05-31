package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import com.rlapcs.radiotransfer.registries.ModItems;
import com.rlapcs.radiotransfer.server.radio.TxMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class TileTxController extends AbstractTileController {
    //~~~~~~~~~~~~~~~~~~~~~Constants~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //Slots and inventory
    public static final int STACK_UPGRADE_SLOT_INDEX = 1;
    public static final int SPEED_UPGRADE_SLOT_INDEX = 2;

    public static final int INVENTORY_SIZE = ABSTRACT_INVENTORY_SIZE + 2;

    //~~~~~~~~~~~~~~~~Instance Variables~~~~~~~~~~~~~~~~~~~~~~~~~//
    private TxMode mode;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public TileTxController() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(STACK_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.STACK_UPGRADE_WHITELIST);
        upgradeSlotWhitelists.put(SPEED_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.SPEED_UPGRADE_WHITELIST);

        mode = TxMode.ROUND_ROBIN;
    }

    public void changeMode() {
        if (mode == TxMode.SEQUENTIAL) {
            mode = TxMode.ROUND_ROBIN;
        } else {
            mode = TxMode.SEQUENTIAL;
        }
        this.markDirty();
        this.onStatusChange();
    }
    public TxMode getMode() {
        return mode;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("roundRobin")) {
            mode = (compound.getBoolean("roundRobin") ? TxMode.ROUND_ROBIN : TxMode.SEQUENTIAL);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("roundRobin", (mode == TxMode.ROUND_ROBIN));

        return compound;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~STATUS UPDATES~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public NBTTagCompound writeStatusToNBT() {
        NBTTagCompound nbt = super.writeStatusToNBT();
        NBTTagList tagList = nbt.getTagList("statuses", Constants.NBT.TAG_COMPOUND);

        tagList.appendTag(new MultiblockStatusData.StatusString("Transmission Mode", mode.getFriendlyName()).toNBT());

        List<MultiblockStatusData.Status> upgradeList = new ArrayList<>();
        upgradeList.add(new MultiblockStatusData.StatusItemStack("Encryption Card", itemStackHandler.getStackInSlot(ENCRYPTION_CARD_SLOT_INDEX)));
        upgradeList.add(new MultiblockStatusData.StatusItemStack("Speed Upgrade", itemStackHandler.getStackInSlot(SPEED_UPGRADE_SLOT_INDEX)));
        upgradeList.add(new MultiblockStatusData.StatusItemStack("Stack Upgrade", itemStackHandler.getStackInSlot(STACK_UPGRADE_SLOT_INDEX)));
        tagList.appendTag(new MultiblockStatusData.StatusList("Upgrades", upgradeList).toNBT());

        nbt.setTag("statuses", tagList);
        return nbt;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //isActive() implemented in AbstractTileController

    //~~~~~~~~~~~~~~~~~~~~Base Power Values~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public int getBasePowerPerTick() {
        return ModConfig.power_options.tx_controller.basePowerPerTick;
    }
    @Override
    public int getBasePowerPerProcess() {
        return ModConfig.power_options.tx_controller.basePowerPerTick;
    }

    //~~~~~~~~~~~~~~~~~~~Calculations~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        Map<Item, Integer> out = new HashMap<>();
        out.put(ModItems.encryption_card, itemStackHandler.getStackInSlot(ENCRYPTION_CARD_SLOT_INDEX).getCount());
        out.put(ModItems.speed_upgrade, itemStackHandler.getStackInSlot(SPEED_UPGRADE_SLOT_INDEX).getCount());

        //Since stack upgrade/downgrade share the same slot, we have to do some extra logic
        ItemStack stackUpgrades = itemStackHandler.getStackInSlot(STACK_UPGRADE_SLOT_INDEX);
        if(stackUpgrades.getItem() == ModItems.stack_upgrade) {
            out.put(ModItems.stack_upgrade, stackUpgrades.getCount());
            out.put(ModItems.stack_downgrade, 0);
        }
        else if(stackUpgrades.getItem() == ModItems.stack_upgrade) {
            out.put(ModItems.stack_upgrade, 0);
            out.put(ModItems.stack_downgrade, stackUpgrades.getCount());
        }
        else {
            out.put(ModItems.stack_upgrade, 0);
            out.put(ModItems.stack_downgrade, 0);
        }

        return out;
    }

    //Average process rate in AbstractTileController

    //~~~~~~~~~~~~~~~~~~~~~~Upgrade Costs~~~~~~~~~~~~~~~~~~~~~~~~~//
    //Constant
    private static final Map<Item, Integer> upgradeConstantPowerCosts;
    static {
        Map <Item, Integer> tempMap = new HashMap<>();

        tempMap.put(ModItems.encryption_card, ModConfig.power_options.tx_controller.encryptionCardCostPerTick);
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.tx_controller.speedUpgradeCostPerTick);
        tempMap.put(ModItems.stack_upgrade, ModConfig.power_options.tx_controller.stackUpgradeCostPerTick);
        tempMap.put(ModItems.stack_downgrade, ModConfig.power_options.tx_controller.stackDowngradeCostPerTick);

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

        tempMap.put(ModItems.encryption_card, ModConfig.power_options.tx_controller.encryptionCardCostPerProcess);
        tempMap.put(ModItems.speed_upgrade, ModConfig.power_options.tx_controller.speedUpgradeCostPerTick);
        tempMap.put(ModItems.stack_upgrade, ModConfig.power_options.tx_controller.stackUpgradeCostPerProcess);
        tempMap.put(ModItems.stack_downgrade, ModConfig.power_options.tx_controller.stackDowngradeCostPerProcess);

        upgradeProcessPowerCosts = Collections.unmodifiableMap(tempMap);
    }
    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return upgradeProcessPowerCosts;
    }
}
