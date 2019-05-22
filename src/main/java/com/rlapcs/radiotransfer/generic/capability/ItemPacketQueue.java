package com.rlapcs.radiotransfer.generic.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class ItemPacketQueue implements IMaterialTransferHandler<ItemStack, ItemPacketQueue.PacketBuffer>, Iterable<ItemPacketQueue.PacketBuffer> {
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

                //item
                NBTTagCompound itemTag = new NBTTagCompound();
                buff.item.writeToNBT(itemTag);
                bufferTag.setTag("item", itemTag);
                //data
                //bufferTag.setInteger("index", i);
                bufferTag.setInteger("quantity", buff.quantity);

                //append to list
                nbtTagList.appendTag(bufferTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("buffers", nbtTagList);
        //nbt.setInteger("size", nbtTagList.tagCount());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        packetBuffers = new ArrayList<>();

        NBTTagList tagList = nbt.getTagList("buffers", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound buffTags = tagList.getCompoundTagAt(i);
            if(buffTags.hasKey("quantity") && buffTags.hasKey("item")) {
                int quantity = buffTags.getInteger("quantity");
                ItemStack item = new ItemStack(buffTags.getCompoundTag("item"));

                packetBuffers.add(new PacketBuffer(item, quantity));
            }
        }
        onLoad();
    }

    @Override
    public boolean isEmpty() {
        for (PacketBuffer b : packetBuffers) {
            if (b != null && !b.isEmpty()) {
                return false;
            }
            else {
                //sendDebugMessage("found and removing empty packetBuffer " + b);
                packetBuffers.remove(b);
                onContentsChanged();
            }
        }

        return true;
    }

    @Override
    public boolean canReceiveDump(PacketBuffer packet) {
        return canAddAny(packet.getItemStack());
    }
    @Override
    public PacketBuffer getIndex(int index) {
        if(!validateIndex(index)) return PacketBuffer.EMPTY;

        PacketBuffer packet = packetBuffers.remove(index);
        onContentsChanged();

        return packet;
    }

    @Override
    public PacketBuffer peekIndex(int index) {
        if(!validateIndex(index)) return PacketBuffer.EMPTY;
        return packetBuffers.get(index);
    }

    @Override
    public int size() {
        return packetBuffers.size();
    }
    @Override
    public Iterator<PacketBuffer> iterator() {
        return packetBuffers.iterator();
    }

    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public ItemStack add(ItemStack stack) {
        return add(stack, Integer.MAX_VALUE);
    }
    @Override
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

    @Override
    public void move(int fromIndex, int toIndex) {
        if (validateIndex(fromIndex) && validateIndex(toIndex)) {
            packetBuffers.add(toIndex, packetBuffers.remove(fromIndex));
        onContentsChanged();
        }
    }

    public List<PacketBuffer> getAsList() {
        List<PacketBuffer> out = new ArrayList<>();
        packetBuffers.forEach(pb -> out.add(pb.copy()));
        return out;
    }

    @Override
    public void onContentsChanged() {
        sendDebugMessage(this.toString());
        //sendDebugMessage("isEmpty?: " + this.isEmpty());
    } //should be overridden to mark tileEntity dirty

    @Override
    public void onLoad() {
    }

    @Override
    public String toString() {
        String s = TextFormatting.DARK_AQUA + "-------PacketQueue-------" + TextFormatting.RESET;
        for (PacketBuffer b : packetBuffers) {
            s += String.format("\n%s|%s  - %-25s", TextFormatting.DARK_AQUA, TextFormatting.RESET, b);
        }
        s += "\n" + TextFormatting.DARK_AQUA + "-------------------------";
        return s;
    }

    public static class PacketBuffer implements IMaterialTransferHandler.Packet<ItemStack> {
        public static final PacketBuffer EMPTY = new PacketBuffer(ItemStack.EMPTY, 0);

        private ItemStack item;
        private int quantity;

        private PacketBuffer(ItemStack item, int quantity) {
            this.item = item.copy();
            this.item.setCount(1);
            this.quantity = quantity;
        }

        @Override
        public ItemStack getMaterial() {
            return getItemStack();
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
        public PacketBuffer copy() {
            return new PacketBuffer(item, quantity);
        }

        @Override
        public String toString() {
            return String.format("%s x%d", item.getUnlocalizedName(), quantity);
        }
    }
}
