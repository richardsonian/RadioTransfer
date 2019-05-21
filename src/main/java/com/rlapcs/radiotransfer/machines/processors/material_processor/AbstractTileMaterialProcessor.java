package com.rlapcs.radiotransfer.machines.processors.material_processor;

import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler;
import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler.Packet;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientDumpablePackets;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTileMaterialProcessor<T extends IMaterialTransferHandler> extends AbstractTileProcessor<T> implements ITileClientUpdater {
    protected List<EntityPlayerMP> clientListeners; //stores on server the clients with the GUI open
    protected boolean[] dumpableData;

    public AbstractTileMaterialProcessor(int itemStackHandlerSize) {
        super(itemStackHandlerSize);
        clientListeners = new ArrayList<>();

        //must init dumpable data in subclass after creating MaterialTransferHandler (like dumpableData = new boolean[packetQueue.size()];) also do this in NBT read
    }

    @Override
    public void doAllClientUpdates() {
        doClientDumpableUpdate();
        doClientPacketQueueUpdate();
    }

    @Override
    public List<EntityPlayerMP> getClientListeners() {
        return clientListeners;
    }

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

    @Override
    public abstract T getHandler();
}
