package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.tileEntities.IProgressBarProvider;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;


public abstract class AbstractTileItemProcessor extends AbstractTileMaterialProcessor<ItemPacketQueue> {
    public static final int INVENTORY_SIZE = 16 + ABSTRACT_MATERIAL_PROCESSOR_INVENTORY_SIZE;

    public static final int BASE_ITEMS_PER_PROCESS = 1;

    protected ItemPacketQueue packetQueue;

    public AbstractTileItemProcessor() {
        super(INVENTORY_SIZE);

        packetQueue = new ItemPacketQueue() {
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                AbstractTileItemProcessor.this.markDirty();

                if(!AbstractTileItemProcessor.this.world.isRemote) {
                    AbstractTileItemProcessor.this.doClientPacketQueueUpdate();

                    //dumpable data update
                    if(getProcessorType() == ProcessorType.ENCODER) {
                        AbstractTileItemProcessor.this.doClientDumpableUpdate();
                        //Debug.sendToAllPlayers("Updated encoder dumpable from self", world);
                    }
                    else {
                        AbstractTileMaterialProcessor encoder = ((AbstractTileMaterialProcessor) AbstractTileItemProcessor.this.getController().getEncoder(AbstractTileItemProcessor.this.getTransferType()));
                        if(encoder != null) encoder.doClientDumpableUpdate();
                        //Debug.sendToAllPlayers("Updated encoder dumpable from decoder", world);
                    }
                }
            }
        };

        dumpableData = new boolean[packetQueue.size()];
    }

    public void debugFillPacketQueue() {
        packetQueue.add(new ItemStack(Items.DIAMOND, 64));
        packetQueue.add(new ItemStack(Items.GOLD_INGOT, 64));
        packetQueue.add(new ItemStack(Items.GOLD_NUGGET, 64));
        packetQueue.add(new ItemStack(Items.GOLDEN_APPLE, 64));
        packetQueue.add(new ItemStack(Items.GHAST_TEAR, 64));
        packetQueue.add(new ItemStack(Items.FEATHER, 64));
        packetQueue.add(new ItemStack(Items.FERMENTED_SPIDER_EYE, 64));
        packetQueue.add(new ItemStack(Items.ACACIA_BOAT, 64));
        packetQueue.add(new ItemStack(Items.APPLE, 64));
    }

    //ProgressBar updates
    protected int getItemsPerProcess() {
        return BASE_ITEMS_PER_PROCESS;
    }

    //NBT handling
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("packets")) {
            packetQueue.deserializeNBT(compound.getCompoundTag("packets"));
        }
        dumpableData = new boolean[packetQueue.size()];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("packets", packetQueue.serializeNBT());

        return compound;
    }


    //Getters and setters
    @Override
    public ItemPacketQueue getHandler() {
        return packetQueue;
    }
    @Override
    public TransferType getTransferType() {
        return TransferType.ITEM;
    }
}
