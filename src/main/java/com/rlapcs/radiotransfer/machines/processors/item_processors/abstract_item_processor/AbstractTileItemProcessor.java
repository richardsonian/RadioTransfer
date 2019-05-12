package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.generic.tileEntities.IProgressBarProvider;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendToAllPlayers;


public abstract class AbstractTileItemProcessor extends AbstractTileMaterialProcessor<ItemPacketQueue> implements IProgressBarProvider {
    public static final int SPEED_UPGRADE_SLOT_INDEX = 0;

    public static final int INVENTORY_SIZE = 17;

    public static final int PROCESS_UPDATE_TICKS = 2;

    public static final int BASE_PROCESS_TIME = 30;
    public static final double PROCESS_TIME_MULTIPLIER = 0.75;
    public static final double MIN_PROCESS_TIME = 6;
    public static final int BASE_ITEMS_PER_PROCESS = 1;

    protected ItemPacketQueue packetQueue;
    protected int processTimeElapsed;

    public AbstractTileItemProcessor() {
        super(INVENTORY_SIZE);

        processTimeElapsed = 0;
        upgradeSlotWhitelists.put(SPEED_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.SPEED_UPGRADE_WHITELIST);

        packetQueue = new ItemPacketQueue() {
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                AbstractTileItemProcessor.this.markDirty();

                if(!AbstractTileItemProcessor.this.world.isRemote) {
                    AbstractTileItemProcessor.this.doClientPacketQueueUpdate();
                    if(getProcessorType() == ProcessorType.ENCODER) {
                        AbstractTileItemProcessor.this.doClientDumpableUpdate();
                    }
                    else {
                        AbstractTileMaterialProcessor encoder = ((AbstractTileMaterialProcessor) AbstractTileItemProcessor.this.getController().getEncoder(AbstractTileItemProcessor.this.getTransferType()));
                        if(encoder != null) encoder.doClientDumpableUpdate();
                    }
                }
            }
        };

        dumpableData = new boolean[packetQueue.size()];
    }

    //ProgressBar updates
    protected int getItemsPerProcess() {
        return BASE_ITEMS_PER_PROCESS;
    }

    @Override
    public int getProcessTime() {
        int numUpgrades = itemStackHandler.getStackInSlot(SPEED_UPGRADE_SLOT_INDEX).getCount();
        int processTime = (int) (BASE_PROCESS_TIME * Math.pow(PROCESS_TIME_MULTIPLIER, numUpgrades));
        return (int) MathHelper.clamp(processTime, MIN_PROCESS_TIME, BASE_PROCESS_TIME);
    }

    @Override
    public void update() {
        super.update();
        //run on both client and server
        if(ticksSinceCreation % PROCESS_UPDATE_TICKS == 0) {
            doProcessUpdate(world, PROCESS_UPDATE_TICKS);
        }
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
    public int getProcessTimeElapsed() {
        return processTimeElapsed;
    }
    @Override
    public void setProcessTimeElapsed(int target) {
        this.processTimeElapsed = target;
    }
    @Override
    public TransferType getTransferType() {
        return TransferType.ITEM;
    }
}
