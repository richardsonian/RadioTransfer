package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import com.rlapcs.radiotransfer.server.radio.TxMode;
import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class TileTxController extends AbstractTileController {
    public static final int STACK_UPGRADE_SLOT_INDEX = 1;
    public static final int SPEED_UPGRADE_SLOT_INDEX = 2;

    public static final int INVENTORY_SIZE = ABSTRACT_INVENTORY_SIZE + 2;

    private TxMode mode;

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

    @Override
    public int getBasePowerPerTick() {
        return ModConfig.power_options.tx_controller.basePowerPerTick;
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
