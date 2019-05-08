package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.tileEntities.IProgressBarProvider;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.registries.ModItems;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

import static com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor.SPEED_UPGRADE_SLOT_INDEX;

public abstract class AbstractTileItemProcessor extends AbstractTileProcessor<ItemPacketQueue> implements IProgressBarProvider {
    public static final int PROCESS_UPDATE_TICKS = 2;

    public static final int BASE_PROCESS_TIME = 30;
    public static final double PROCESS_TIME_MULTIPLIER = 0.75;
    public static final double MIN_PROCESS_TIME = 6;
    public static final int BASE_ITEMS_PER_PROCESS = 1;


    protected ItemPacketQueue packetQueue;
    protected int processTimeElapsed;

    public AbstractTileItemProcessor(int itemStackHandlerSize) {
        super(itemStackHandlerSize);

        packetQueue = new ItemPacketQueue() {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                AbstractTileItemProcessor.this.markDirty();
            }
        };
        processTimeElapsed = 0;
    }

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

    @Override
    protected boolean isItemValidInSlot(int slot, @Nonnull ItemStack stack) {
        //CHANGE THIS TO REFERENCE BLACKLIST
        //sendDebugMessage("Validating slot " + slot + " for " + stack);
        if(slot == SPEED_UPGRADE_SLOT_INDEX) {
            return stack.getItem() == ModItems.redgem;
        }
        else {
            return true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("packets")) {
            packetQueue.deserializeNBT(compound.getCompoundTag("packets"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("packets", packetQueue.serializeNBT());

        return compound;
    }

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
