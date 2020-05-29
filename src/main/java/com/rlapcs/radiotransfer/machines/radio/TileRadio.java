package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachineWithInventory;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientTileMultiblockStatusData;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileRadio extends AbstractTileMachineWithInventory implements ITileClientUpdater {
    public final int MULTIBLOCK_UPDATE_TICKS = 20;
    public final int REGISTER_UPDATE_TICKS = 20;
    private final int SEND_RESOURCES_UPDATE_TICKS = 20;
    private static final int POWER_CHECK_TICKS = 1;


    protected MultiblockRadioController multiblock;
    protected MultiblockStatusData multiblockStatusData; //Client only-server side isn't updated
    protected Set<EntityPlayerMP> clientListeners;

    public TileRadio() {
        super(0);

        multiblock = new MultiblockRadioController(this);
        multiblockStatusData = new MultiblockStatusData();
        clientListeners = new HashSet<>();
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~STATUS DATA~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public Set<EntityPlayerMP> getClientListeners() {
        return clientListeners;
    }

    public void updateMultiblockStatusData(AbstractTileMultiblockNode te) {
        NBTTagCompound nbt = te.writeStatusToNBT();
        clientListeners.forEach(p -> ModNetworkMessages.INSTANCE.sendTo(new MessageUpdateClientTileMultiblockStatusData(this.getPos(), nbt), p));
    }

    public void updateMultiblockStatusData(List<AbstractTileMultiblockNode> tes) {
        //Agregate all the nodes into a single NBT tag and send to the client.
        NBTTagList tagList = new NBTTagList();
        for(AbstractTileMultiblockNode te : tes) {
            tagList.appendTag(te.writeStatusToNBT());
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("node_status_list", tagList);

        clientListeners.forEach(p -> ModNetworkMessages.INSTANCE.sendTo(new MessageUpdateClientTileMultiblockStatusData(this.getPos(), nbt), p));
    }

    @Override
    public void doAllClientUpdates() { //A bit of a hack to use this method, but it should be called on initGui so it works
        updateMultiblockStatusData(multiblock.getAllActiveNodes());
    }

    //Client Side Only
    public MultiblockStatusData getMultiblockStatusData() {return multiblockStatusData;}
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~RESOURCE SENDING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    private void sendResources() {
        if(multiblock.canTransmit(TransferType.ITEM)) {
            boolean success = RadioNetwork.INSTANCE.sendItems(multiblock, 16, multiblock.getTransmitMode(TransferType.ITEM));
            if(success) {
                multiblock.getTxController().doProcess();
            }
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~TILE ENTITY FUNCTION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            multiblock.deregisterFromNetwork();
            multiblock.deregisterAllNodes();
        }
    }

    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if(!world.isRemote) {
            multiblock.deregisterFromNetwork();
            multiblock.deregisterAllNodes();
        }
    }

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                multiblock.checkForNewNodes(this.pos);
            }
            if(ticksSinceCreation % REGISTER_UPDATE_TICKS == 0 && !multiblock.isRegisteredToNetwork()) {
                multiblock.registerToNetwork();
            }
            if(ticksSinceCreation % SEND_RESOURCES_UPDATE_TICKS == 0) {
                sendResources();
            }
            if(ticksSinceCreation % POWER_CHECK_TICKS == 0) {
                //Debug.sendToAllPlayers(String.format("%s[CONST PWR]%s %dFE/t x %dt = %dFE", TextFormatting.YELLOW, TextFormatting.GRAY, multiblock.calculateRequiredPowerPerTick(), POWER_CHECK_TICKS, (multiblock.calculateRequiredPowerPerTick() * POWER_CHECK_TICKS)), world);
                //Debug.sendToAllPlayers(multiblock.hasSufficientConstantPower(POWER_CHECK_TICKS) ? "Has enough power" : "Not enough power",world);
                //use power from nodes
                if(multiblock.hasSufficientConstantPower(POWER_CHECK_TICKS)) {

                    if(!multiblock.isPowered()) multiblock.setPowered(true);
                    multiblock.useConstantPower(POWER_CHECK_TICKS); //assuming we don't have to check whether this was successful, because we already checked that there was enough power. If something went wrong, we'll catch it in the next cycle.
                }
                else {
                    multiblock.setPowered(false);
                    //Debug.sendToAllPlayers(TextFormatting.DARK_RED + this.toString() + " unpowered.", world);
                }
            }
        }
    }

    public MultiblockRadioController getMultiblockController() {
        return multiblock;
    }


}
