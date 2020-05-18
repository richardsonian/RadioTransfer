package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientMultiblockNodeRegistered;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTileMultiblockNodePowered;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractTileMultiblockNode extends AbstractTileMachine {
    //~~~~~~~~~~~~~~~~~~Constants~~~~~~~~~~~~~~~~~~~~//
    public static final int MULTIBLOCK_UPDATE_TICKS = 20;
    public static final int AVERAGE_PROCESS_CALC_TICKS = 40; //only needed in nodes which complete processes
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //~~~~~~~~~~~~~~Instance Variables~~~~~~~~~~~~~~~//
    //server
    protected boolean registeredInMultiblock;
    protected MultiblockRadioController controller;

    //client
    protected boolean cachedPowered; //use controller.isPowered() for server side check
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public AbstractTileMultiblockNode() {
        registeredInMultiblock = false;
        cachedPowered = false;
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MULTIBLOCK REGISTRY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
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
    /**
     * Sets the registered state without performing any logic
     */
    public void setRegisteredInMultiblock(boolean target) {
        registeredInMultiblock = target;
    }

    public void updateClientsRegisteredState(boolean target) {
        int dimension = this.world.provider.getDimension();
        ModNetworkMessages.INSTANCE.sendToAllTracking(new MessageUpdateClientMultiblockNodeRegistered(this, target), new NetworkRegistry.TargetPoint(
                dimension, pos.getX(), pos.getY(), pos.getZ(), -1
        ));
    }

    public MultiblockRadioController getController() {
        return controller;
    }
    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MULTIBLOCK DETECTION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
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

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Power Calculations~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//

    //~~~~~~~~~~Abstract Methods to be overridden by individual machines~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Returns whether currently using power or not, process or constant
     * @return
     */
    public abstract boolean isActive();
    public abstract int getBasePowerPerTick();
    public abstract Map<Item, Integer> getUpgradeCardConstantPowerCosts();
    public abstract int getBasePowerPerProcess();
    public abstract Map<Item, Integer> getUpgradeCardProcessPowerCosts();
    public abstract Map<Item, Integer> getUpgradeCardQuantities();
    /**
     * Return average processes per tick over last n ticks, or -1 if this node does not complete processes.
     * n should reference AbstractTileMultiblockNode#AVERAGE_PROCESS_CALC_TICKS
     * @return The process rate
     */
    public abstract double getAverageProcessesRate();


    //~~~~~~~~~~~~~~~~~~~~~~~Universal Calculations for Power Data~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //- Move some of this to MultiblockPowerData object, so it can be calculated on call instead of sending over network?
    public int getPowerPerTick() {
        if(isActive()) { //Only require constant power if active
            return getBasePowerPerTick() + getConstantPowerUpgradeTotalCost();
        }
        else {
            return 0;
        }
    }
    public int getPowerPerProcess() { //This is triggered only when active anyways, so we don't need to check
        return getBasePowerPerProcess() + getProcessPowerUpgradeTotalCost();
    }
    public double getAverageProcessPowerPerTick() {
        if(getAverageProcessesRate() != -1) {
            return getPowerPerProcess() * getAverageProcessesRate();
        }
        else {
            return 0;
        }
    }
    public int getConstantPowerUpgradeTotalCost() {
        int sum = 0;
        for(Item k : getConstantPowerContributingUpgrades()) {
            sum += (getUpgradeCardConstantPowerCosts().get(k) * getUpgradeCardQuantities().get(k));
        }
        return sum;
    }
    public int getProcessPowerUpgradeTotalCost() {
        int sum = 0;
        for(Item k : getProcessPowerContributingUpgrades()) {
            sum += (getUpgradeCardProcessPowerCosts().get(k) * getUpgradeCardQuantities().get(k));
        }
        return sum;
    }
    public Set<Item> getConstantPowerContributingUpgrades() {
        //Return empty set if there is no entry present
        if(getUpgradeCardQuantities() == null) return new HashSet<>();

        //Find all upgrade cards that do not have a quantity of 0
        Set<Item> presentUpgrades = getUpgradeCardQuantities().keySet();
        presentUpgrades.removeIf((k) -> getUpgradeCardQuantities().get(k) == 0);
        //filter out those that do not have a power cost value
        presentUpgrades.removeIf((k) -> !getUpgradeCardConstantPowerCosts().containsKey(k));

        return presentUpgrades;
    }
    public Set<Item> getProcessPowerContributingUpgrades() {
        //Return empty set if there is no entry present
        if(getUpgradeCardQuantities() == null) return new HashSet<>();

        //Find all upgrade cards that do not have a quantity of 0
        Set<Item> presentUpgrades = getUpgradeCardQuantities().keySet();
        presentUpgrades.removeIf((k) -> getUpgradeCardQuantities().get(k) == 0);
        //filter out those that do not have a power cost value
        presentUpgrades.removeIf((k) -> !getUpgradeCardProcessPowerCosts().containsKey(k));

        return presentUpgrades;
    }
    //~~~~~~~~~~~~~~~~~~~~~Power Using Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    /**
     * Use power for one process
     * If there is not enough power, calls MultiblockRadioController#setPowered(false) [see method doc for side effect]
     * @return Whether there was enough power for process or not
     */
    public boolean useProcessPower() {
        int needed = this.getPowerPerProcess();
        //Debug.sendToAllPlayers(String.format("%s[PROCESS PWR]%s %s using %dFE", TextFormatting.AQUA, TextFormatting.RESET, getClass().getSimpleName(), needed), world);
        if(controller.hasPowerForProcess(needed)) {
            return controller.useProcessPower(needed); //extra check returning this val
        }
        //if not enough power for process
        //Debug.sendToAllPlayers(String.format("%s[PROCESS PWR]%s Not enough power for %s%s's%s process.", TextFormatting.AQUA, TextFormatting.DARK_RED, TextFormatting.RESET, getClass().getSimpleName(), TextFormatting.DARK_RED), world);
        controller.setPowered(false);
        return false;
    }

    //~~~~~~~~~~~~~~~~~~Client Cached Powered State~~~~~~~~~~~~~~~~~~~~//
    //Client only
    public void setClientPowered(boolean target) {
        this.cachedPowered = target;
        Debug.sendToAllPlayers(TextFormatting.GRAY + "[CLIENT]" + this.getClass().getSimpleName() + (target ? " powered." : " unpowered."), world);
    }
    public boolean getClientPowered() {
        return cachedPowered;
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~TileEntity Data Caching~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        if(!world.isRemote) { //assure server side
            tag.setBoolean("registeredInMultiblock", registeredInMultiblock); //make sure client has latest registration info when they load the te
            if(registeredInMultiblock && controller != null)
                tag.setBoolean("powered", controller.isPowered());
            else
                tag.setBoolean("powered", false);
        }
        return tag;
    }
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        if(world.isRemote) { //assure client side
            if(tag.hasKey("registeredInMultiblock")) {
                setRegisteredInMultiblock(tag.getBoolean("registeredInMultiblock")); //make sure client has latest registration info when they load the te
                setClientPowered(tag.getBoolean("powered"));
            }
        }
    }
    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            this.deregisterFromMultiblock(); //Deregister TE from multiblock when block broken
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

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MISC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
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
