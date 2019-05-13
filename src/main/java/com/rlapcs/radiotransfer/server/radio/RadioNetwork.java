package com.rlapcs.radiotransfer.server.radio;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import net.minecraft.item.ItemStack;

import java.util.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public enum RadioNetwork {
    INSTANCE;

    public static final int MAX_PRIORITY = 99;
    public static final int MIN_PRIORITY = 1;

    public static final int MIN_FREQUENCY = 1;
    public static final int MAX_FREQUENCY = 5;

    private HashSet<MultiblockRadioController> radios;
    private Map<TransferType, Map<MultiblockRadioController, PriorityQueue<MultiblockRadioController>>> radioSendQueues;

    private RadioNetwork() {
        radios = new HashSet();
        radioSendQueues = new EnumMap<>(TransferType.class);
        for(TransferType t : TransferType.values()) {
            radioSendQueues.put(t, new HashMap<>());
        }
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
     * Resets the sendQueue for the given radio and type. Returns false if there are no other radios to add.
     * @param radio
     * @return
     */
    private boolean resetSendQueue(MultiblockRadioController radio, TransferType type) {
        //if there are no other radios or the radio cannot transmit on the specified type, set an empty priority queue
        if(radios.size() <= 1 || !radio.canTransmit(type))  {
            radioSendQueues.get(type).put(radio, new PriorityQueue<>());
            return false;
        }

        //copy the receivers list
        Set<MultiblockRadioController> radiosCopy = (Set<MultiblockRadioController>) radios.clone();
        radiosCopy.remove(radio); //remove self from the queue

        //remove any on a different frequency, that cant receive, or that are invalid
        int txFreq = radio.getTransmitFrequency(type);
        radiosCopy.removeIf((r)-> !r.canReceive(type) || r.getReceiveFrequency(type)!=txFreq || radio.getTileEntity().isInvalid()); //also test activation here?

        // Make a new PriorityQueue with ordering relative to the transmitter
        PriorityQueue<MultiblockRadioController> pq = new PriorityQueue<>(1, (r1, r2)-> {
            if(r1.getReceivePriority(type) == r2.getReceivePriority(type)) {
                return (int) (radio.getTileEntity().getDistanceSq(r1.getTileEntity().getPos().getX(), r1.getTileEntity().getPos().getY(), r1.getTileEntity().getPos().getZ())
                        - radio.getTileEntity().getDistanceSq(r2.getTileEntity().getPos().getX(), r2.getTileEntity().getPos().getY(), r2.getTileEntity().getPos().getZ()));
                }
                else {
                    return r2.getReceivePriority(type) - r1.getReceivePriority(type);
                }
        });

        //add all the receivers
        pq.addAll(radiosCopy);

        //sendDebugMessage("Created new priority queue for " + radio + " : " + pq);
        //add the new list
        radioSendQueues.get(type).put(radio, pq);
        return true;
    }


    public boolean sendItems(MultiblockRadioController sender, int amount, TxMode mode) {
        if(mode == TxMode.SEQUENTIAL) return false;
        else if(mode == TxMode.ROUND_ROBIN) return sendItemsRoundRobin(sender, amount);
        else return false;
    }

    /*
     * Returns whether something was sent
    */
    public boolean sendItemsRoundRobin(MultiblockRadioController sender, int amount) { //test for repeated action?
        if(!sender.canTransmit(TransferType.ITEM)) return false;

        //make new queue if one doesn't exist or has been exhausted
        if(radioSendQueues.get(TransferType.ITEM).get(sender) == null || radioSendQueues.get(TransferType.ITEM).get(sender).size() == 0){
            //sendDebugMessage("Resetting send queue for radio: " + radio);
            boolean queueHasContents = resetSendQueue(sender, TransferType.ITEM);
            if(!queueHasContents) return false;
        }

        //poll the priority queue until a valid receiver is found or the queue runs out
        MultiblockRadioController receiver = null;
        while(receiver == null || !radios.contains(receiver) || receiver.getTileEntity().isInvalid()
                || !receiver.canReceive(TransferType.ITEM)
                || receiver.getReceiveFrequency(TransferType.ITEM) != sender.getTransmitFrequency(TransferType.ITEM)) { //short circuit avoid null pointer
            //check multiblockValid?

            if(radioSendQueues.get(TransferType.ITEM).get(sender).size() > 0) {
                receiver = radioSendQueues.get(TransferType.ITEM).get(sender).poll();
            }
            else {
                return false; //none left in queue
            }
        }

        //sendDebugMessage("Sending to: " + radio);

        //note: would be nicer to peek packet and then change quantity instead of removing and readding!
        ItemPacketQueue senderPacketQueue = (ItemPacketQueue) sender.getTransmitHandler(TransferType.ITEM);
        ItemPacketQueue receiverPacketQueue = (ItemPacketQueue) receiver.getReceiveHandler(TransferType.ITEM);

        ItemStack toSend = senderPacketQueue.getNextPacket(amount);
        sendDebugMessage("Sending itemStack " + toSend + " to " + receiver);
        ItemStack remainder = receiverPacketQueue.add(toSend);
        sendDebugMessage("Returning remainder: " + remainder + " items to " + sender);
        senderPacketQueue.add(remainder);

        return ItemStack.areItemStacksEqual(toSend, remainder); //whether something was sent
    }
}