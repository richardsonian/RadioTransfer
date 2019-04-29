package com.rlapcs.radiotransfer.server.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.MultiblockRadioController;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.TileTransmitter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public enum RadioNetwork {
    INSTANCE;

    public static final int MAX_PRIORITY = 99;
    public static final int MIN_PRIORITY = 1;

    public static final int MIN_FREQUENCY = 1;
    public static final int MAX_FREQUENCY = 5;

    private HashSet<MultiblockRadioController> radios;
    private Map<MultiblockRadioController, PriorityQueue<MultiblockRadioController>> radioSendQueues;

    private RadioNetwork() {
        radios = new HashSet();
        radioSendQueues = new HashMap<>();
    }

    public void register(MultiblockRadioController radio) {
        radios.add(radio);
        //sendDebugMessage("registering " + radio);
    }
    public void deregister(MultiblockRadioController radio) {
        radios.remove(radio);
        //sendDebugMessage("deregistering " + radio);
    }

    /**
     * Resets the sendQueue for the given transmitter. Returns false if there are no receivers to add.
     * @param radio
     * @return
     */
    private boolean resetSendQueue(MultiblockRadioController radio) {
        //if there are no receivers, set an empty priority queue
        if(radios.size() == 0)  {
            radioSendQueues.put(radio, new PriorityQueue<>());
            return false;
        }

        //copy the receivers list
        Set<MultiblockRadioController> radiosCopy = (Set<MultiblockRadioController>) radios.clone();
        radiosCopy.remove(radio); //remove self from the queue

        //remove any on a different frequency or that are invalid
        int sendFreq = radio.
        receiversCopy.removeIf((r)->r.getFrequency()!=freq || r.isInvalid()); //also test activation here?

        // Make a new PriorityQueue with ordering relative to the transmitter
        PriorityQueue<MultiblockRadioController> pq = new PriorityQueue<>(1, (r1, r2)-> {
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
        MultiblockRadioController tileReceiver = null;
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
}