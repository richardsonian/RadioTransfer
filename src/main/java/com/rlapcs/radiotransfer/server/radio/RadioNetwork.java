package com.rlapcs.radiotransfer.server.radio;

import com.rlapcs.radiotransfer.machines.deprecated.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines.deprecated.transmitter.TileTransmitter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public enum RadioNetwork {
    INSTANCE;

    public static final int MAX_PRIORITY = 99;
    public static final int MIN_PRIORITY = 1;

    private HashSet<TileReceiver> receivers;
    private Map<TileTransmitter, PriorityQueue<TileReceiver>> transmitterSendQueues;

    private RadioNetwork() {
        receivers = new HashSet();
        transmitterSendQueues = new HashMap<>();
    }

    public void register(TileReceiver receiver) {
        receivers.add(receiver);
        //sendDebugMessage("registering " + receiver);
    }
    public void deregister(TileReceiver receiver) {
        receivers.remove(receiver);
        //sendDebugMessage("deregistering " + receiver);
    }

    /**
     * Resets the sendQueue for the given transmitter. Returns false if there are no receivers to add.
     * @param trans
     * @return
     */
    private boolean resetSendQueue(TileTransmitter trans) {
        //if there are no receivers, set an empty priority queue
        if(receivers.size() == 0)  {
            transmitterSendQueues.put(trans, new PriorityQueue<>());
            return false;
        }

        //copy the receivers list
        Set<TileReceiver> receiversCopy = (Set<TileReceiver>) receivers.clone();

        //remove any on a different frequency or that are invalid
        int freq = trans.getFrequency();
        receiversCopy.removeIf((r)->r.getFrequency()!=freq || r.isInvalid()); //also test activation here?

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
    public boolean sendItems(TileTransmitter tileTransmitter, int slot, int amount) { //test for repeated action?
        //if receivers empty, don't bother doing logic
        if(receivers.size() <= 0) return false;

        //make new queue if one doesn't exist or has been exhausted
        if(transmitterSendQueues.get(tileTransmitter) == null || transmitterSendQueues.get(tileTransmitter).size() == 0){
            //sendDebugMessage("Resetting send queue for transmitter: " + tileTransmitter);
            boolean queueHasContents = resetSendQueue(tileTransmitter);
            if(!queueHasContents) return false;
        }

        //poll the priority queue until a valid receiver is found or the queue runs out
        TileReceiver tileReceiver = null;
        while(tileReceiver == null || !receivers.contains(tileReceiver) || tileReceiver.isInvalid()
                || !tileReceiver.getActivated() || tileReceiver.getFrequency() != tileTransmitter.getFrequency()) { //short circuit avoid null pointer

            if(transmitterSendQueues.get(tileTransmitter).size() > 0) {
                tileReceiver = transmitterSendQueues.get(tileTransmitter).poll();
            }
            else {
                return false; //none left in queue
            }
        }

        //sendDebugMessage("Sending to: " + tileReceiver);

        ItemStackHandler senderInv = (ItemStackHandler) tileTransmitter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        ItemStack slotStack = senderInv.getStackInSlot(slot);
        int amountToSend = MathHelper.clamp(slotStack.getCount(), 0, amount);

        ItemStack toSend = slotStack.copy();
        toSend.setCount(amountToSend);
        sendDebugMessage("Sending itemStack " + toSend + " to " + tileReceiver);
        int remainderCount = tileReceiver.mergeStackIntoInventory(toSend).getCount();
        sendDebugMessage("Returning remainder of " + remainderCount + " items to slot " + slot + " in " + tileTransmitter);
        slotStack.shrink(amountToSend - remainderCount);

        return amount != remainderCount; //whether something was sent
    }

    private static void sendDebugMessage(String msg) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(msg));
    }
}