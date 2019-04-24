package com.rlapcs.radiotransfer.server.radio;

import com.rlapcs.radiotransfer.machines.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines.transmitter.TileTransmitter;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

import static com.rlapcs.radiotransfer.RadioTransfer.MODID;

public enum RadioRegistry {
    INSTANCE;

    public static final int MAX_PRIORITY = 99;
    public static final int MIN_PRIORITY = 1;

    private List<TileTransmitter> transmitters;
    private List<TileReceiver> receivers;
    private Map<TileTransmitter, RoundRobinTracker<TileReceiver>> roundRobinTrackerMap;

    private RadioRegistry() {
        receivers = new ArrayList<>();
        transmitters = new ArrayList<>();
        roundRobinTrackerMap = new HashMap<>();
    }

    public void register(TileTransmitter trans) {
        transmitters.add(trans);
    }

    public void register(TileReceiver receiver) {
        receivers.add(receiver);
    }
    public void deregister(TileTransmitter trans) {
        transmitters.remove(trans);
    }

    public void deregister(TileReceiver receiver) {
        receivers.remove(receiver);
    }


    private void resetRoundRobinTracker(TileTransmitter trans) {
        //copy the receivers list
        List<TileReceiver> sortedReceivers = (List<TileReceiver>) ((ArrayList<TileReceiver>)receivers).clone();

        //remove any on a different frequency
        int freq = trans.getFrequency();
        sortedReceivers.removeIf((r)->r.getFrequency()!=freq);

        //sort by priority and distance
        sortedReceivers.sort((r1, r2)-> {
            if(r1.getPriority() == r2.getPriority()) {
                return (int) (trans.getDistanceSq(r1.getPos().getX(), r1.getPos().getY(), r1.getPos().getZ())
                        - trans.getDistanceSq(r2.getPos().getX(), r2.getPos().getY(), r2.getPos().getZ()));
                }
                else {
                    return r2.getPriority() - r1.getPriority();
                }
        });

        //add the new list
        roundRobinTrackerMap.put(trans, new RoundRobinTracker<>(sortedReceivers));
    }

    public void sendItems(TileTransmitter tileTransmitter, int slot, int amount) {
        ItemStackHandler senderInv = (ItemStackHandler) tileTransmitter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if(roundRobinTrackerMap.get(tileTransmitter) == null || roundRobinTrackerMap.get(tileTransmitter).isComplete()) { //short circuit to avoid nullpointerexception
            resetRoundRobinTracker(tileTransmitter);
        }

        TileReceiver tileReceiver = roundRobinTrackerMap.get(tileTransmitter).getNext();

        //implement only sending amount param
        ItemStack toSend = senderInv.getStackInSlot(slot);
        System.out.println("Sending itemStack " + toSend + " to " + tileReceiver);
        ItemStack remainder = tileReceiver.mergeStackIntoInventory(toSend);
        System.out.println("Returning remainder stack: " + remainder + " to slot " + slot);
        senderInv.setStackInSlot(slot, remainder);

    }
}