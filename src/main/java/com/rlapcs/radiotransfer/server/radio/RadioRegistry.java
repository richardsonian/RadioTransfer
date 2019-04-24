package com.rlapcs.radiotransfer.server.radio;

import com.rlapcs.radiotransfer.machines.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines.transmitter.TileTransmitter;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public enum RadioRegistry {
    INSTANCE;

    public static final int MAX_PRIORITY = 99;
    public static final int MIN_PRIORITY = 1;

    private HashSet<TileTransmitter> transmitters;
    private HashSet<TileReceiver> receivers;
    private Map<TileTransmitter, PriorityQueue<TileReceiver>> transmitterSendQueues;

    private RadioRegistry() {
        receivers = new HashSet();
        transmitters = new HashSet<>();
        transmitterSendQueues = new HashMap<>();
    }

    public void register(TileTransmitter trans) {
        transmitters.add(trans);
    }
    public void register(TileReceiver receiver) {
        receivers.add(receiver);
        sendDebugMessage("registering " + receiver);
    }
    public void deregister(TileTransmitter trans) {
        transmitters.remove(trans);
    }
    public void deregister(TileReceiver receiver) {
        receivers.remove(receiver);
        sendDebugMessage("deregistering " + receiver);
    }

    /**
     * Resets the sendQueue for the given transmitter. Returns false if there are no receivers to add.
     * @param trans
     * @return
     */
    private boolean resetSendQueue(TileTransmitter trans) {
        if(receivers.size() == 0)  {
            transmitterSendQueues.put(trans, new PriorityQueue<>()); //put something there to avoid null pointer when queuing size
            return false;
        }

        //copy the receivers list
        Set<TileReceiver> receiversCopy = (Set<TileReceiver>) receivers.clone();

        //remove any on a different frequency or that are invalid
        int freq = trans.getFrequency();
        receiversCopy.removeIf((r)->r.getFrequency()!=freq || r.isInvalid());

        // Make a new PriorityQueue with ordering relative to the transmitter
        PriorityQueue<TileReceiver> pq = new PriorityQueue<>(1, (r1, r2)-> {
            if(r1.getPriority() == r2.getPriority()) {
                return (int) (trans.getDistanceSq(r1.getPos().getX(), r1.getPos().getY(), r1.getPos().getZ())
                        - trans.getDistanceSq(r2.getPos().getX(), r2.getPos().getY(), r2.getPos().getZ()));
                }
                else {
                    return r2.getPriority() - r1.getPriority();
                }
        });

        //add all the receivers
        pq.addAll(receiversCopy);

        //sendDebugMessage("Created new priority queue for " + trans + " : " + pq);
        //add the new list
        transmitterSendQueues.put(trans, pq);
        return true;
    }

    /**
     * Returns whether something was sent
     * @param tileTransmitter
     * @param slot
     * @param amount
     */
    public boolean sendItems(TileTransmitter tileTransmitter, int slot, int amount) {
        //if receivers empty, don't bother doing logic
        if(receivers.size() <= 0) return false;

        ItemStackHandler senderInv = (ItemStackHandler) tileTransmitter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        //make new queue if one doesn't exist or has been exhausted
        if(transmitterSendQueues.get(tileTransmitter) == null || transmitterSendQueues.get(tileTransmitter).size() == 0){
            //sendDebugMessage("Resetting send queue for transmitter: " + tileTransmitter);
            boolean queueHasContents = resetSendQueue(tileTransmitter);
            if(!queueHasContents) return false;
        }

        //pull the next valid receiver from the queue
        TileReceiver tileReceiver = null;
        while(tileReceiver == null || !receivers.contains(tileReceiver) || tileReceiver.isInvalid() || !tileReceiver.getActivated()) { //short circuit avoid null pointer
            if(transmitterSendQueues.get(tileTransmitter).size() > 0) {
                tileReceiver = transmitterSendQueues.get(tileTransmitter).poll();
            }
            else {
                return false; //none left in queue
            }
        }

        //sendDebugMessage("Sending to: " + tileReceiver);

        //implement only sending amount param
        ItemStack toSend = senderInv.getStackInSlot(slot);
        //sendDebugMessage("Sending itemStack " + toSend + " to " + tileReceiver);
        ItemStack remainder = tileReceiver.mergeStackIntoInventory(toSend);
        //sendDebugMessage("Returning remainder stack: " + remainder + " to slot " + slot + " in " + tileTransmitter);
        senderInv.setStackInSlot(slot, remainder);

        return ItemStack.areItemStacksEqual(toSend, remainder); //whether something was sent
    }

    private static void sendDebugMessage(String msg) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(msg));
    }
}