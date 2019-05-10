package com.rlapcs.radiotransfer.generic.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public class ItemPacketQueue implements INBTSerializable<NBTTagCompound>, ITransferHandler, Iterable<ItemPacketQueue.PacketBuffer> {
    public static final int MAX_QUANTITY = 999;
    public static final int MAX_BUFFERS = 15;

    private List<PacketBuffer> packetBuffers;

    public ItemPacketQueue() {
        packetBuffers = new ArrayList<>();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < packetBuffers.size(); i++) {
            PacketBuffer buff = packetBuffers.get(i);
            if (buff != null && !buff.isEmpty()) {
                NBTTagCompound bufferTag = new NBTTagCompound();
                bufferTag.setInteger("index", i);
                buff.item.writeToNBT(bufferTag);
                bufferTag.setInteger("quantity", buff.quantity);
                nbtTagList.appendTag(bufferTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("items", nbtTagList);
        nbt.setInteger("size", packetBuffers.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        packetBuffers = new ArrayList<>();
        NBTTagList tagList = nbt.getTagList("items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound buffTags = tagList.getCompoundTagAt(i);
            if (buffTags.hasKey("index") && buffTags.hasKey("quantity")) {
                int index = buffTags.getInteger("index");
                int quantity = buffTags.getInteger("quantity");
                if (index >= 0 && index < packetBuffers.size()) {
                    packetBuffers.set(index, new PacketBuffer(new ItemStack(buffTags), quantity));
                }
            }
        }
        onLoad();
    }

    @Override
    public boolean isEmpty() {
        for (PacketBuffer b : packetBuffers) {
            if (b != null && !b.isEmpty()) {
                return false;
            } else {
                packetBuffers.remove(b);
                onContentsChanged();
            }
        }
        return true;
    }

    public int size() {
        return packetBuffers.size();
    }

    public Iterator<PacketBuffer> iterator() {
        return packetBuffers.iterator();
    }

    public ItemStack peekNextPacket(int maxAmount) {
        for (PacketBuffer b : packetBuffers) {
            if (b != null && !b.isEmpty()) {
                ItemStack stack = b.item.copy();
                int numToGive = MathHelper.clamp(b.quantity, 1, Math.min(maxAmount, stack.getMaxStackSize()));
                stack.setCount(numToGive);

                return stack;
            } else { //trim nulls
                packetBuffers.remove(b);
                onContentsChanged();
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getNextPacket(int maxAmount) {
        for (PacketBuffer b : packetBuffers) {
            if (b != null && !b.isEmpty()) {
                ItemStack stack = b.item.copy();
                int numToGive = MathHelper.clamp(b.quantity, 1, Math.min(maxAmount, stack.getMaxStackSize()));
                stack.setCount(numToGive);
                b.quantity -= numToGive;
                if (b.quantity == 0) packetBuffers.remove(b);
                onContentsChanged();
                return stack;
            } else {
                packetBuffers.remove(b);
                onContentsChanged();
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean canAddAll(ItemStack stack, int maxAmount) {
        if (stack.isEmpty()) return false;
        if (maxAmount <= 0) throw new InvalidParameterException("maxAmount must be 1 or greater.");

        int effectiveAmount = Math.min(stack.getCount(), maxAmount);
        int countLeft = effectiveAmount;

        //see if can merge in existing buffer
        for (PacketBuffer b : packetBuffers) {
            if (ItemHandlerHelper.canItemStacksStack(b.item, stack)) {
                countLeft -= (MAX_QUANTITY - b.quantity);
                if (countLeft <= 0) return true;
            }
        }
        //if there's an extra buffer, we can add!
        if (packetBuffers.size() < MAX_BUFFERS) {
            int buffersLeft = MAX_BUFFERS - packetBuffers.size();
            if (countLeft <= buffersLeft * MAX_QUANTITY) return true;
        }

        //if cant fit in new buffers
        return false;
    }

    public boolean canAddAny(ItemStack stack) {
        if (stack.isEmpty()) return false;

        //if there's an extra buffer, we can add!
        if (packetBuffers.size() < MAX_BUFFERS) {
            return true;
        }
        //see if can merge in existing buffer
        for (PacketBuffer b : packetBuffers) {
            if (ItemHandlerHelper.canItemStacksStack(b.item, stack)) {
                if (b.quantity < MAX_QUANTITY) {
                    return true;
                }
            }
        }
        //if cant fit in anywhere
        return false;
    }

    public ItemStack add(ItemStack stack) {
        return add(stack, Integer.MAX_VALUE);
    }

    public ItemStack add(ItemStack stack, int maxAmount) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (maxAmount <= 0)
            throw new InvalidParameterException("Cannot have a max amount that is less than or equal to 0!");

        ItemStack staq = stack.copy();

        //try to merge into existing buffers
        for (PacketBuffer b : packetBuffers) {
            if (ItemHandlerHelper.canItemStacksStack(b.item, staq)) {
                int count = MathHelper.clamp(staq.getCount(), 0, maxAmount);
                int numToAdd = MathHelper.clamp(count, 0, MAX_QUANTITY - b.quantity);
                sendDebugMessage("Adding " + numToAdd + " of " + staq);
                b.quantity += numToAdd;
                staq.shrink(numToAdd);
                this.onContentsChanged();
                return staq;
            }
        }
        //if wasn't merged into a previous buffer, make a new one
        if (packetBuffers.size() < MAX_BUFFERS) {
            int count = MathHelper.clamp(staq.getCount(), 0, maxAmount);
            int numToAdd = MathHelper.clamp(count, 0, MAX_QUANTITY);
            packetBuffers.add(new PacketBuffer(stack, numToAdd));
            this.onContentsChanged();
            staq.shrink(numToAdd);
            return (staq.isEmpty() ? ItemStack.EMPTY : staq);
        }

        //if cant make a new buffer, return the original parameter
        return stack;
    }

    public void move(int fromIndex, int toIndex) {
        packetBuffers.add(toIndex, packetBuffers.remove(fromIndex));
        onContentsChanged();
    }

    public List<PacketBuffer> getAsList() {
        return packetBuffers;
    }

    protected void onContentsChanged() {
        //sendDebugMessage(this.toString());
    } //should be overridden to mark tileEntity dirty

    protected void onLoad() {
    }

    @Override
    public String toString() {
        String s = "--PacketQueue--";
        for (PacketBuffer b : packetBuffers) {
            s += "\n" + b;
        }
        return s;
    }

    public static class PacketBuffer {
        public static final PacketBuffer EMPTY = new PacketBuffer(ItemStack.EMPTY, 0);

        private ItemStack item;
        private int quantity;

        private PacketBuffer(ItemStack item, int quantity) {
            this.item = item.copy();
            this.item.setCount(1);
            this.quantity = quantity;
        }

        private PacketBuffer(ItemStack stack) {
            this(stack, stack.getCount());
        }

        public boolean isEmpty() {
            return (quantity <= 0) || item.isEmpty();
        }

        public ItemStack getItemStack() {
            ItemStack stack = item.copy();
            stack.setCount(quantity);
            return stack;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return String.format("Packets: %s -- x%d", item.getUnlocalizedName(), quantity);
        }
    }
}
