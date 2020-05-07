package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientMultiblockNodeRegistered;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractTileMultiblockNode extends AbstractTileMachine {
    public static final int MULTIBLOCK_UPDATE_TICKS = 20;

    protected boolean registeredInMultiblock;
    protected MultiblockRadioController controller;

    public AbstractTileMultiblockNode() {
        registeredInMultiblock = false;
    }

    public void registerInMultiblock(MultiblockRadioController controller) {
        if(!registeredInMultiblock) {
            this.controller = controller;
            registeredInMultiblock = true;
            sendDebugMessage(TextFormatting.GREEN + " " + this + " registered " + TextFormatting.RESET + " to: " + controller);

            updateClientsRegisteredState(true);
            onRegisterInMultiblock();
        }
    }
    public void deregisterFromMultiblock() {
        if (registeredInMultiblock) {
            sendDebugMessage(TextFormatting.RED + " " + this + " deregistered " + TextFormatting.RESET + " from: " + controller);
            registeredInMultiblock = false; //must come before notify surrounding
            this.notifySurroundingDetatch();
            controller.removeNode(this);
            this.controller = null; //must come after notify surrounding

            updateClientsRegisteredState(false);
            onDeregisterInMultiblock();
        }
    }
    public boolean isRegisteredInMultiblock() {
        return registeredInMultiblock;
    }
    protected void onRegisterInMultiblock() {}
    protected void onDeregisterInMultiblock() {}
    /*Sets the registered state without performing any logic */
    public void setRegisteredInMultiblock(boolean target) {
        registeredInMultiblock = target;
    }

    public void updateClientsRegisteredState(boolean target) {
        int dimension = this.world.provider.getDimension();
        ModNetworkMessages.INSTANCE.sendToAllTracking(new MessageUpdateClientMultiblockNodeRegistered(this, target), new NetworkRegistry.TargetPoint(
                dimension, pos.getX(), pos.getY(), pos.getZ(), -1
        ));
    }

    //make sure client has latest registration info when they load the te
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        if(!world.isRemote) { //assure server side
            tag.setBoolean("registeredInMultiblock", registeredInMultiblock);
        }
        return tag;
    }
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        if(world.isRemote) { //assure client side
            if(tag.hasKey("registeredInMultiblock")) {
                setRegisteredInMultiblock(tag.getBoolean("registeredInMultiblock"));
            }
        }
    }

    //invalidate
    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            this.deregisterFromMultiblock();
        }
    }
    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if(!world.isRemote) {
            this.deregisterFromMultiblock();
        }
    }

    private boolean isAdjacentToController() {
        BlockPos[] neighbors = {pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};

        for(BlockPos n : neighbors) {
            TileEntity te = world.getTileEntity(n);

            if(te instanceof TileRadio) {
                if(((TileRadio) te).getMultiblockController() == controller) {
                    //sendDebugMessage(this + " is adjacent to " + controller);
                    return true;
                }
            }
        }
        return false;
    }
    private List<AbstractTileMultiblockNode> getNeighboringNodes() {
        List<AbstractTileMultiblockNode> nodes = new ArrayList<>();

        BlockPos[] neighbors = {pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};
        for(BlockPos n : neighbors) {
            TileEntity te = world.getTileEntity(n);
            if(te instanceof AbstractTileMultiblockNode) {
                AbstractTileMultiblockNode node = (AbstractTileMultiblockNode) te;
                //sendDebugMessage("found neighboring multiblock tile " + node + (node.isRegisteredInMultiblock() ? "(registered)" : "(deregistered)")+ " with controller" + node.getController());
                if(node.isRegisteredInMultiblock() && (node.getController() == this.getController())) {
                    //sendDebugMessage("adding " + node + "to neighbor list");
                    nodes.add(node);
                }
            }
        }
        //sendDebugMessage("found neighboring nodes: " + nodes);
        return nodes;
    }
    private boolean isConnectedToController(List<AbstractTileMultiblockNode> alreadyBeingChecked) { //recursion :)
        if(this.isAdjacentToController()) return true;
        else {
            //so that it doesn't contain nodes already asked if they're already asking
            alreadyBeingChecked.add(this);
            List<AbstractTileMultiblockNode> nodes = getNeighboringNodes();
            nodes.removeAll(alreadyBeingChecked);

            for(AbstractTileMultiblockNode node : nodes) {
                if(node.isConnectedToController(alreadyBeingChecked)) {
                    //sendDebugMessage(this + " is connected to its controller");
                    return true;
                }
            }
            return false;
        }
    }
    private void notifySurroundingDetatch() {
        for(AbstractTileMultiblockNode node : getNeighboringNodes()) {
            //sendDebugMessage(this + " notified detatch to " + node);
            node.verifyConnectedToController();
        }
    }
    private void verifyConnectedToController() {
        if(registeredInMultiblock) {
            if(!isConnectedToController(new ArrayList<>())) {
                this.deregisterFromMultiblock();
            }
        }
    }

    public MultiblockRadioController getController() {
        return controller;
    }

    /*~~~~~~~~~~~Power Calculations~~~~~~~~~~*/
    public abstract int getPowerUsagePerTick(); //will remove

    public abstract int getBasePowerPerTick();
    public abstract Map<Item, Integer> getUpgradeCardConstantPowerCosts();

    public abstract int getBasePowerPerProcess();
    public abstract Map<Item, Integer> getUpgradeCardProcessPowerCosts();

    public abstract Map<Item, Integer> getUpgradeCardQuantities();
    public abstract int getAverageProcessesRate(); //return num processes in last 10 seconds

    public int getPowerPerTick() {
        return getBasePowerPerTick() + getConstantPowerUpgradeTotalCost();
    }
    public int getPowerPerProcess() {
        return getBasePowerPerProcess() + getProcessPowerUpgradeTotalCost();
    }
    public int getAverageProcessPowerPerTick() {
        return getPowerPerProcess() + getAverageProcessesRate();
    }
    public int getConstantPowerUpgradeTotalCost() {
        int sum = 0;
        for(Item k : getConstantPowerContributingUpgrades()) {
            sum += getUpgradeCardConstantPowerCosts().get(k);
        }
        return sum;
    }
    public int getProcessPowerUpgradeTotalCost() {
        int sum = 0;
        for(Item k : getConstantPowerContributingUpgrades()) {
            sum += getUpgradeCardConstantPowerCosts().get(k);
        }
        return sum;
    }
    public Set<Item> getConstantPowerContributingUpgrades() {
        //Find all upgrade cards that do not have a quantity of 0
        Set<Item> presentUpgrades = getUpgradeCardQuantities().keySet();
        presentUpgrades.removeIf((k) -> getUpgradeCardQuantities().get(k) == 0);
        //filter out those that do not have a power cost value
        presentUpgrades.removeIf((k) -> !getUpgradeCardConstantPowerCosts().containsKey(k));

        return presentUpgrades;
    }
    public Set<Item> getProcessPowerContributingUpgrades() {
        //Find all upgrade cards that do not have a quantity of 0
        Set<Item> presentUpgrades = getUpgradeCardQuantities().keySet();
        presentUpgrades.removeIf((k) -> getUpgradeCardQuantities().get(k) == 0);
        //filter out those that do not have a power cost value
        presentUpgrades.removeIf((k) -> !getUpgradeCardProcessPowerCosts().containsKey(k));

        return presentUpgrades;
    }


    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (this.registeredInMultiblock && ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                controller.checkForNewNodes(this.pos);
            }
        }
    }
}
