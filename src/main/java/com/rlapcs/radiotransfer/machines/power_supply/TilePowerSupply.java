package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockPowerUsageData;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.generic.tileEntities.ITilePowerBarProvider;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTileMultiblockPowerData;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTilePowerBar;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TilePowerSupply extends AbstractTileMultiblockNodeWithInventory implements ITileClientUpdater, ITilePowerBarProvider {
    //~~~~~~~~~~~~~~~~~~~~~~~CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~//
    public static final int INVENTORY_SIZE = 1;
    public static final int POWER_ITEM_INDEX = 0;

    public static final int POWER_ITEM_UPDATE_TICKS = 20;
    public static final int POWER_BAR_CLIENT_UPDATE_TICKS = 10;
    public static final int MULTIBLOCK_POWER_DATA_CLIENT_UPDATE_TICKS = 20;

    //move to config
    public static final int ENERGY_CAPACITY = 10000; //FE

    //~~~~~~~~~~~~~~~~~~INSTANCE VARS~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //for client
    protected int displayEnergy;
    protected MultiblockPowerUsageData cachedPowerUsageData;

    //for server
    protected MachinePowerHandler energyStorage;
    protected Set<EntityPlayerMP> clientListeners;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public TilePowerSupply() {
        super(INVENTORY_SIZE);

        //server
        clientListeners = new HashSet<>();
        upgradeSlotWhitelists.put(POWER_ITEM_INDEX, ModConstants.UpgradeCards.POWER_ITEM_WHITELIST); //"upgrade card" lol
        energyStorage = new MachinePowerHandler(ENERGY_CAPACITY, this);

        //client
        displayEnergy = 0;
        cachedPowerUsageData = new MultiblockPowerUsageData();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~POWER CAPABILITY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
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
                    int space = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                    int extracted = itemEnergyStorage.extractEnergy(space, false);
                    energyStorage.receiveEnergy(extracted, false);
                }
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~CLIENT UPDATES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public Set<EntityPlayerMP> getClientListeners() {
        return clientListeners;
    }

    public void updateClientPowerBar() {
        if(!clientListeners.isEmpty()) {
            //Debug.sendToAllPlayers(TextFormatting.GRAY + "Sending power bar to clients with power: " + energyStorage.getEnergyStored(), world);
            //need to change power input
            clientListeners.forEach((p) -> ModNetworkMessages.INSTANCE.sendToAll(new MessageUpdateClientTilePowerBar(this, energyStorage.getEnergyStored())));
        }
    }
    public void updateClientMultiblockPowerData() {
        if(registeredInMultiblock && !clientListeners.isEmpty()) {
            //Debug.sendToAllPlayers(TextFormatting.GRAY + "Sending multiblock power update to clients", world);
            //Tell controller to update data
            this.getController().updatePowerUsageData();
            //Send message
            clientListeners.forEach((p) -> ModNetworkMessages.INSTANCE.sendToAll(new MessageUpdateClientTileMultiblockPowerData(this)));
        }
    }

    @Override
    public void doAllClientUpdates() {
        updateClientPowerBar();
        updateClientMultiblockPowerData();
    }

    //CLIENT ONLY Getters and setters for cached energy data
    public int getDisplayEnergy() { return displayEnergy;}
    public void setDisplayEnergy(int target) { displayEnergy = target;}
    public MultiblockPowerUsageData getCachedPowerUsageData() {
        return cachedPowerUsageData;
    }
    @Override
    public int getMaxEnergy() {
        return ENERGY_CAPACITY;
    } //might have to fix this once implemented into config
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~TE UPDATE / DATA~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("energyStorage")) {
            energyStorage.deserializeNBT(compound.getCompoundTag("energyStorage"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("energyStorage", energyStorage.serializeNBT());
        return compound;
    }
    @Override
    public void update() {
        super.update();

        if(!world.isRemote) { //server side updates
            if (ticksSinceCreation % POWER_BAR_CLIENT_UPDATE_TICKS == 0) {
                updateClientPowerBar();
            }
            if (ticksSinceCreation % MULTIBLOCK_POWER_DATA_CLIENT_UPDATE_TICKS == 0) {
                updateClientMultiblockPowerData();
            }
            if (ticksSinceCreation % POWER_ITEM_UPDATE_TICKS == 0) {
                getEnergyFromPowerItem(POWER_ITEM_UPDATE_TICKS);
            }
        }
        else { //client side
        }
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~PSU POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    // No intention for the PSU to require any power to run atm, but these fields are required as an AbstractTileMultiblockNode

    @Override
    public int getBasePowerPerTick() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardConstantPowerCosts() {
        return Collections.emptyMap();
    }

    @Override
    public int getBasePowerPerProcess() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return Collections.emptyMap();
    }

    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        return Collections.emptyMap();
    }

    @Override
    public double getAverageProcessesRate() {
        return -1; //does not do processes
    }
}
