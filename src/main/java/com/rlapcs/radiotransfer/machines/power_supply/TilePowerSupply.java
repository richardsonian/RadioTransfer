package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTilePowerData;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;

public class TilePowerSupply extends AbstractTileMultiblockNodeWithInventory implements ITileClientUpdater {
    public static final int INVENTORY_SIZE = 1;
    public static final int ENERGY_CAPACITY = 10000; //FE
    public static final int MAX_ENERGY_TRANSFER = 100; //FE/t

    public static final int POWER_ITEM_INDEX = 0;

    public static final int POWER_ITEM_UPDATE_TICKS = 40;
    public static final int POWER_VISUAL_UPDATE_TICKS = 2;
    public static final int POWER_CLIENT_UDPATE_TICKS = 20;

    //for client
    protected int displayEnergy;
    protected int cachedPowerUsage;
    protected int cachedPowerGain;

    //for server
    protected EnergyStorage energyStorage;
    protected Set<EntityPlayerMP> clientListeners;

    public TilePowerSupply() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(POWER_ITEM_INDEX, ModConstants.UpgradeCards.POWER_ITEM_WHITELIST); //"upgrade card" lol

        energyStorage = new EnergyStorage(ENERGY_CAPACITY);
        clientListeners = new HashSet<>();
        energyStorage.receiveEnergy(5000, false);

        //client
        displayEnergy = energyStorage.getEnergyStored();
        cachedPowerUsage = 0;
        cachedPowerGain = 0;
    }

    public int extractEnergy(int amount, boolean simulate) {
        return energyStorage.extractEnergy(amount, simulate);
    }

    protected void getEnergyFromPowerItem(int ticksSinceLastUpdate) {
        ItemStack powerItem = itemStackHandler.getStackInSlot(POWER_ITEM_INDEX);
        if(powerItem != null && !powerItem.isEmpty()) {
            if(powerItem.hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage itemEnergyStorage = powerItem.getCapability(CapabilityEnergy.ENERGY, null);
                if(itemEnergyStorage.canExtract() && energyStorage.canReceive()) {
                    int space = MathHelper.clamp(energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored(), 0, (MAX_ENERGY_TRANSFER * ticksSinceLastUpdate));
                    int extracted = itemEnergyStorage.extractEnergy(space, false);
                    energyStorage.receiveEnergy(extracted, false);
                }
            }
        }
    }
    public int getMaxPowerPerTickFromItem() {
        ItemStack powerItem = itemStackHandler.getStackInSlot(POWER_ITEM_INDEX);
        if(powerItem != null && !powerItem.isEmpty()) {
            if (powerItem.hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage itemEnergyStorage = powerItem.getCapability(CapabilityEnergy.ENERGY, null);
                return itemEnergyStorage.extractEnergy(MAX_ENERGY_TRANSFER, true);
            }
        }
        return 0;
    }

    @Override
    public int getPowerUsagePerTick() {
        return 0;
    }



    @Override
    public void update() {
        super.update();

        if(!world.isRemote) {
            if (ticksSinceCreation % POWER_CLIENT_UDPATE_TICKS == 0) {
                updateClientPowerData();
            }
            if (ticksSinceCreation % POWER_ITEM_UPDATE_TICKS == 0) {
                getEnergyFromPowerItem(POWER_ITEM_UPDATE_TICKS);
            }
        }
        else { //client side
            if(ticksSinceCreation % POWER_VISUAL_UPDATE_TICKS == 0) {
                updateClientVisualPower(POWER_VISUAL_UPDATE_TICKS);
            }
        }
    }

    @Override
    public Set<EntityPlayerMP> getClientListeners() {
        return clientListeners;
    }

    public void updateClientPowerData() {
        clientListeners.forEach((p) -> ModNetworkMessages.INSTANCE.sendToAll(new MessageUpdateClientTilePowerData(this, energyStorage.getEnergyStored(),
                getMaxPowerPerTickFromItem(), controller.calculatePowerUsagePerTick())));
    }

    @Override
    public void doAllClientUpdates() {
        updateClientPowerData();
    }

    //CLIENT ONLY Getters and setters for cached energy transfer
    public void updateClientVisualPower(int ticksSinceLastUpdate) {
        int effectiveRate = (cachedPowerGain - cachedPowerUsage);
        displayEnergy += effectiveRate * ticksSinceLastUpdate;
    }

    public int getDisplayEnergy() { return displayEnergy;}
    public void setDisplayEnergy(int target) { displayEnergy = target;}
    public double getDisplayEnergyAsFraction() {return ((double) displayEnergy / (double) energyStorage.getMaxEnergyStored());}

    public int getCachedPowerUsage() {
        return cachedPowerUsage;
    }
    public void setCachedPowerUsage(int cachedPowerUsage) {
        this.cachedPowerUsage = cachedPowerUsage;
    }
    public int getCachedPowerGain() {
        return cachedPowerGain;
    }
    public void setCachedPowerGain(int cachedPowerGain) {
        this.cachedPowerGain = cachedPowerGain;
    }
}
