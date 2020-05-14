package com.rlapcs.radiotransfer.machines.processors.material_processor;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler;
import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler.Packet;
import com.rlapcs.radiotransfer.generic.tileEntities.IProgressBarProvider;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientDumpablePackets;
import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTileMaterialProcessor<T extends IMaterialTransferHandler> extends AbstractTileProcessor<T> implements ITileClientUpdater, IProgressBarProvider {
    //~~~~~~~~~~~~~~~~~~~~~~~CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //Inventory
    public static final int SPEED_UPGRADE_SLOT_INDEX = 0;
    public static final int ABSTRACT_MATERIAL_PROCESSOR_INVENTORY_SIZE = 1;

    //Process -- move these to config soon
    public static final int PROCESS_UPDATE_TICKS = 2;
    public static final int BASE_PROCESS_TIME = 30;
    public static final double PROCESS_TIME_MULTIPLIER = 0.75;
    public static final double MIN_PROCESS_TIME = 6;

    //~~~~~~~~~~~~~~~~~~INSTANCE VARIABLES~~~~~~~~~~~~~~~~~~~~~~~~//
    protected Set<EntityPlayerMP> clientListeners; //stores on server the clients with the GUI open
    protected boolean[] dumpableData; //only used in encoders, but we want this code across all MaterialProcessors
    protected int processTimeElapsed; //for Progress Bar

    //used to calculate AbstractTileMultiblockNode#getAverageProcessRate()
    protected int processesCompletedInCycle;
    protected double lastAverageProcessRate;

    public AbstractTileMaterialProcessor(int itemStackHandlerSize) {
        super(itemStackHandlerSize);

        //for server
        clientListeners = new HashSet<>();
        upgradeSlotWhitelists.put(SPEED_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.SPEED_UPGRADE_WHITELIST);
        processTimeElapsed = 0;
        processesCompletedInCycle = 0;
        lastAverageProcessRate = 0.0;

        //must init dumpable data in subclass after creating MaterialTransferHandler (like dumpableData = new boolean[packetQueue.size()];) also do this in NBT read
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~Client Updates~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public void doAllClientUpdates() {
        doClientDumpableUpdate();
        doClientPacketQueueUpdate();
    }

    @Override
    public Set<EntityPlayerMP> getClientListeners() {
        return clientListeners;
    }

    //client updates
    public void doClientPacketQueueUpdate() {
        clientListeners.forEach(p -> {
            ModNetworkMessages.INSTANCE.sendTo(new MessageUpdateClientPacketQueue(this), p);
        });
    }

    //very type unsafe lol
    public void doClientDumpableUpdate() {
        if(this.getProcessorType() == ProcessorType.ENCODER && this.isRegisteredInMultiblock()) {
            int size = getHandler().size();
            boolean[] data = new boolean[size];

            AbstractTileProcessor decoderUncast = this.getController().getDecoder(this.getTransferType());
            if (decoderUncast == null) { //also checks null
                sendListenersDumpableMessage(data); //send all false
                return;
            }

            IMaterialTransferHandler decoderHandler = (IMaterialTransferHandler) decoderUncast.getHandler();
            List<Packet> ourPackets = getHandler().getAsList();
            for (int i = 0; i < size; i++) {
                if(decoderHandler.canReceiveDump(ourPackets.get(i))) {
                    data[i] = true;
                }
                else {
                    data[i] = false;
                }
            }
            sendListenersDumpableMessage(data);
        }
    }
    private void sendListenersDumpableMessage(boolean[] data) {
        clientListeners.forEach(p ->
                ModNetworkMessages.INSTANCE.sendTo(new MessageUpdateClientDumpablePackets(this, data), p)
        );
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~DUMP DATA~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public boolean dump(int index) {
        if(getProcessorType() == ProcessorType.ENCODER && registeredInMultiblock) {
            AbstractTileProcessor decoderUncast = this.getController().getDecoder(this.getTransferType());
            if(decoderUncast instanceof AbstractTileMaterialProcessor && decoderUncast.getProcessorType() == ProcessorType.DECODER) {
                AbstractTileMaterialProcessor decoder = (AbstractTileMaterialProcessor) decoderUncast;

                if(decoder.getHandler().canReceiveDump(this.getHandler().peekIndex(index))) {
                    this.getHandler().add(decoder.getHandler().add(this.getHandler().getIndex(index).getMaterial()));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean[] getDumpableData() {
        if(this.getProcessorType() == ProcessorType.ENCODER) {
            return dumpableData;
        }
        return null;
    }

    public void setDumpableData(boolean[] dumpableData) {
        if(this.getProcessorType() == ProcessorType.ENCODER) {
            this.dumpableData = dumpableData;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~Process Methods~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public boolean canDoProcess() {
        return isRegisteredInMultiblock() /*&& getController().isPowered()*/; //causes null pointer exception on client (referencing controller)
    }
    @Override
    public void doProcess() {
        processesCompletedInCycle++;
        this.useProcessPower();
    }

    @Override
    public int getProcessTime() {
        int numUpgrades = itemStackHandler.getStackInSlot(SPEED_UPGRADE_SLOT_INDEX).getCount();
        int processTime = (int) (BASE_PROCESS_TIME * Math.pow(PROCESS_TIME_MULTIPLIER, numUpgrades));
        return (int) MathHelper.clamp(processTime, MIN_PROCESS_TIME, BASE_PROCESS_TIME);
    }
    @Override
    public int getProcessTimeElapsed() {
        return processTimeElapsed;
    }
    @Override
    public void setProcessTimeElapsed(int target) {
        this.processTimeElapsed = target;
    }

    //Used for power display calculations
    @Override
    public double getAverageProcessesRate() {
        return lastAverageProcessRate;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~MISC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Override
    public void update() {
        super.update();
        //~~~~Server Side Updates~~~~//
        if(!world.isRemote) {
            //Refresh average process rate
            if(ticksSinceCreation % AVERAGE_PROCESS_CALC_TICKS == 0) {
                lastAverageProcessRate = (double) processesCompletedInCycle / (double) AVERAGE_PROCESS_CALC_TICKS;
                processesCompletedInCycle = 0;
            }
        }
        //~~~~Client Side Updates~~~~//
        else {}

        //~~~~Both Side Update~~~~//
        if (ticksSinceCreation % PROCESS_UPDATE_TICKS == 0) {
            doProcessUpdate(world, PROCESS_UPDATE_TICKS);
        }
    }

    @Override
    public abstract T getHandler();
}
