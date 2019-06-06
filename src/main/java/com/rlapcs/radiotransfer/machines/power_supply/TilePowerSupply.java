package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.generic.tileEntities.ITilePowerBarProvider;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTilePowerData;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;

public class TilePowerSupply extends AbstractTileMultiblockNodeWithInventory implements ITileClientUpdater, ITilePowerBarProvider {
    public static final int INVENTORY_SIZE = 1;
    public static final int ENERGY_CAPACITY = 10000; //FE
    public static final int MAX_ENERGY_TRANSFER = 100; //FE/t

    public static final int POWER_ITEM_INDEX = 0;

    public static final int POWER_ITEM_UPDATE_TICKS = 40;
    public static final int POWER_VISUAL_UPDATE_TICKS = 2;
    public static final int POWER_CLIENT_UDPATE_TICKS = 20;

    //for client
    protected int displayEnergy;
    protected double cachedEnergyUsage;
    protected double cachedEnergyGain;

    //for server
    protected MachinePowerHandler energyStorage;
    protected Set<EntityPlayerMP> clientListeners;

    public TilePowerSupply() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(POWER_ITEM_INDEX, ModConstants.UpgradeCards.POWER_ITEM_WHITELIST); //"upgrade card" lol

        energyStorage = new MachinePowerHandler(ENERGY_CAPACITY, MAX_ENERGY_TRANSFER, POWER_CLIENT_UDPATE_TICKS, this);
        clientListeners = new HashSet<>();
        //energyStorage.receiveEnergy(5000, false);

        //client
        displayEnergy = energyStorage.getEnergyStored();
        cachedEnergyUsage = 0;
        cachedEnergyGain = 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyStorage);
        }
        return super.getCapability(capability, facing);
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
        Debug.sendToAllPlayers(TextFormatting.GRAY + "Sending power update to clients with receiverate: " + energyStorage.getReceiveRate() + " and extractrate: " + energyStorage.getExtractRate(), world);
        //need to change power input
        clientListeners.forEach((p) -> ModNetworkMessages.INSTANCE.sendToAll(new MessageUpdateClientTilePowerData(this, energyStorage.getEnergyStored(),
                energyStorage.getReceiveRate(), energyStorage.getExtractRate())));
    }

    @Override
    public void doAllClientUpdates() {
        updateClientPowerData();
    }

    //CLIENT ONLY Getters and setters for cached energy transfer
    public int getDisplayEnergy() { return displayEnergy;}
    public void setDisplayEnergy(int target) { displayEnergy = target;}
    public double getCachedEnergyUsage() {
        return cachedEnergyUsage;
    }
    public double getCachedEnergyGain() {
        return cachedEnergyGain;
    }
    public void setCachedEnergyUsage(double target) {
        cachedEnergyUsage = target;
    }
    public void setCachedEnergyGain(double target) {
        cachedEnergyGain = target;
    }

    //internal only
    @Override
    public MachinePowerHandler getEnergyStorage() {
        return energyStorage;
    }
}
