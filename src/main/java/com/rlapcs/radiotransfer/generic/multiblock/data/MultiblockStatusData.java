package com.rlapcs.radiotransfer.generic.multiblock.data;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

/**
 * This class is intended only to be relevant on the client.
 * Update messages are sent from TileRadio / AbstractTileMultiblockNode whenever a status changes, including the entire
 * set of statuses for that node. On GUI load, a full set of statuses are sent.
 * (unlike MultiblockPowerData, which is pretty poorly written and sends full updates of all data every second or so)
 */
public class MultiblockStatusData {
    //~~~~~~~~~~~~~~~Static values~~~~~~~~~~~~~~~~~~//
    //entry ordering
    public static final Comparator<NodeStatusEntry> ENTRY_ORDERING = Comparator.comparingInt(n -> ModConstants.NODE_ORDER.indexOf(n.block));

    //~~~~~~~~~~~~~~~Instance Vars~~~~~~~~~~~~~~~~//
    private Set<NodeStatusEntry> entries;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public MultiblockStatusData() {
        entries = new HashSet<>();
    }

    public void readNodeFromNBT(NBTTagCompound nbt) {
        boolean entryAlreadyExisted = false;
        //loop through entries to see if one with the same pos already exists
        for(NodeStatusEntry e : entries) {
            if(nbt.hasKey("pos")) {
                if(BlockPos.fromLong(nbt.getLong("pos")).equals(e.pos)) {
                    //If this is a remove message, remove the entry
                    if(nbt.hasKey("removeme") && nbt.getBoolean("removeme")) {
                        entries.remove(e);
                        //Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.DARK_RED + "Removed status entry " + TextFormatting.RESET + e.getBlock().getLocalizedName());
                    }
                    //otherwise, update it with the nbt
                    else {
                        e.readFromNBT(nbt);
                        //Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.BLUE + "Updated status entry " + TextFormatting.RESET + e.getBlock().getLocalizedName());
                        //Debug: Show the contents of the status entry
                        //Debug.sendDebugMessage(e.toString());
                    }
                    entryAlreadyExisted = true;
                }
            }
        }
        //Add this as a new entry if not already included
        if(!entryAlreadyExisted) {
            NodeStatusEntry newEntry = new NodeStatusEntry(nbt);
            entries.add(newEntry);
            //Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.GREEN + "Added new status entry " + TextFormatting.RESET + newEntry.getBlock().getLocalizedName());
            //Debug: Show the contents of the status entry
            //Debug.sendDebugMessage(newEntry.toString());
        }
    }

    /**
     * Get the positions of all the nodes. Does not include position of the radio.
     * @return a HashSet of all the node positions
     */
    public List<BlockPos> getAllNodePositions() {
        List<BlockPos> out = new ArrayList<>();
        for(NodeStatusEntry e : entries) {
            out.add(e.pos);
        }
        return out;
    }
    public List<NodeStatusEntry> getSortedEntries() { //is this needed?
        List<NodeStatusEntry> outList = new ArrayList<>(entries);
        outList.sort(ENTRY_ORDERING);
        return outList;
    }
    public NodeStatusEntry getEntry(Block block) {
        for(NodeStatusEntry e:entries) {
            if(e.block.equals(block)) return e;
        }
        return null;
    }
    public NodeStatusEntry getEntry(BlockPos pos) {
        for(NodeStatusEntry e:entries) {
            if(e.pos.equals(pos)) return e;
        }
        return null;
    }

    public static class NodeStatusEntry {
        private BlockPos pos;
        private Block block;
        private List<Status> statuses;

        public NodeStatusEntry() {
            statuses = new ArrayList<>();
        }
        public NodeStatusEntry(NBTTagCompound nbt) {
            this();
            this.readFromNBT(nbt);
        }
        public void readFromNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("pos")) {
                pos = BlockPos.fromLong(nbt.getLong("pos"));
            }
            if(nbt.hasKey("block")) {
                block = Block.getBlockFromName(nbt.getString("block"));
            }
            if(nbt.hasKey("statuses")) {
                statuses = new ArrayList<>();
                NBTTagList tagList = nbt.getTagList("statuses", Constants.NBT.TAG_COMPOUND);
                for(int i = 0; i < tagList.tagCount(); i++) {
                    statuses.add(Status.fromNBT((NBTTagCompound) tagList.get(i)));
                }
            }
        }

        public BlockPos getPos() {
            return pos;
        }
        public Block getBlock() {
            return block;
        }
        public List<Status> getStatuses() {
            return statuses;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append(TextFormatting.UNDERLINE + "NodeStatus - " + block.getLocalizedName());
            str.append(String.format("%s\nPOS:[%d, %d, %d]", TextFormatting.RESET, pos.getX(), pos.getY(), pos.getZ()));
            for(Status s : statuses) {
                str.append("\n - " + s);
            }
            return str.toString();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~STATUS CLASSES~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public abstract static class Status<T> {
        //Codes for types in NBT
        public static final int INT = 0;
        public static final int DOUBLE = 1;
        public static final int BOOL = 2;
        public static final int STRING = 3; //Use this for enums
        public static final int FRACTION = 4;
        public static final int ITEMSTACK = 5;
        public static final int LIST = 6;

        //Data
        protected String key;
        protected T value;

        public Status(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public static Status fromNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("key") && nbt.hasKey("type") && nbt.hasKey("value")) {
                String key = nbt.getString("key");
                switch(nbt.getInteger("type")) {
                    case INT: return new StatusInt(key, nbt.getInteger("value"));
                    case DOUBLE: return new StatusDouble(key, nbt.getDouble("value"));
                    case BOOL: return new StatusBool(key, nbt.getBoolean("value"));
                    case STRING: return new StatusString(key, nbt.getString("value"));
                    case FRACTION: return new StatusFraction(key, (NBTTagCompound) nbt.getTag("value"));
                    case ITEMSTACK: return new StatusItemStack(key, (NBTTagCompound) nbt.getTag("value"));
                    case LIST: return new StatusList(key, nbt.getTagList("value", Constants.NBT.TAG_COMPOUND));
                }
            }
            return null;
        }

        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("key", key);

            return nbt;
        }


        @Override
        public String toString() {
            return String.format("%s: %s", key, getFormattedValue());
        }

        public String getFormattedValue() {
            return value.toString();
        }

        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public T getValue() {
            return value;
        }
        public void setValue(T value) {
            this.value = value;
        }
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~STATUS SUBCLASSES~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public static class StatusInt extends Status<Integer> {
        public StatusInt(String key, Integer value) {
            super(key, value);
        }

        @Override
        public String getFormattedValue() {
            return super.getFormattedValue();
        }

        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", INT);
            nbt.setInteger("value", value);
            return nbt;
        }
    }
    public static class StatusDouble extends Status<Double> {
        public StatusDouble(String key, Double value) {
            super(key, value);
        }
        @Override
        public String getFormattedValue() {
            return super.getFormattedValue();
        }

        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", DOUBLE);
            nbt.setDouble("value", value);
            return nbt;
        }
    }
    public static class StatusBool extends Status<Boolean> {
        public StatusBool(String key, Boolean value) {
            super(key, value);
        }
        @Override
        public String getFormattedValue() {
            return (value ? "yes" : "no");
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", BOOL);
            nbt.setBoolean("value", value);
            return nbt;
        }
    }
    public static class StatusString extends Status<String> {
        public StatusString(String key, String value) {
            super(key, value);
        }
        @Override
        public String getFormattedValue() {
            return super.getFormattedValue();
        }

        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", STRING);
            nbt.setString("value", value);
            return nbt;
        }
    }
    public static class StatusFraction extends Status<StatusFraction.Fraction> { //test if this apache dependency causes problems
        public StatusFraction(String key, Fraction value) {
            super(key, value);
        }
        public StatusFraction(String key, NBTTagCompound valueNBT) {
            this(key, new Fraction(valueNBT));
        }
        public StatusFraction(String key, int numerator, int denominator) {
            this(key, new Fraction(numerator, denominator));
        }

        @Override
        public String getFormattedValue() {
            return super.getFormattedValue();
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", FRACTION);
            nbt.setTag("value", value.serializeNBT());
            return nbt;
        }
        public static class Fraction implements INBTSerializable<NBTTagCompound> {
            private int numerator;
            private int denominator;

            public Fraction(int numerator, int denominator) {
                this.numerator = numerator;
                this.denominator = denominator;
            }
            public Fraction(NBTTagCompound nbt) {
                this(0, 0);
                this.deserializeNBT(nbt);
            }

            @Override
            public NBTTagCompound serializeNBT() {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("numerator", numerator);
                nbt.setInteger("denominator", denominator);
                return nbt;
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt) {
                if(nbt.hasKey("numerator")) {
                    numerator = nbt.getInteger("numerator");
                }
                if(nbt.hasKey("denominator")) {
                    denominator = nbt.getInteger("denominator");
                }
            }

            @Override
            public String toString() {
                return (numerator + "/" + denominator);
            }

            public int getNumerator() {
                return numerator;
            }
            public void setNumerator(int numerator) {
                this.numerator = numerator;
            }
            public int getDenominator() {
                return denominator;
            }
            public void setDenominator(int denominator) {
                this.denominator = denominator;
            }
            public void set(int numerator, int denominator) {
                this.numerator = numerator;
                this.denominator = denominator;
            }
        }
    }
    public static class StatusItemStack extends Status<ItemStack> {
        public StatusItemStack(String key, ItemStack value) {
            super(key, value);
        }
        public StatusItemStack(String key, NBTTagCompound valueNBT) {
            super(key, new ItemStack(valueNBT));
        }
        @Override
        public String getFormattedValue() {
            return value.getDisplayName() + " ×" + value.getCount();
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", ITEMSTACK);
            nbt.setTag("value", value.serializeNBT());
            return nbt;
        }
        public int getQuantity() {
            return value.getCount();
        }
    }
    public static class StatusList extends Status<List<Status>> {
        public StatusList(String key, List<Status> value) {
            super(key, value);
        }
        public StatusList(String key, NBTTagList value) {
            super(key, new ArrayList<>());
            for(int i = 0; i < value.tagCount(); i++) {
                this.value.add(Status.fromNBT((NBTTagCompound) value.get(i)));
            }
        }
        @Override
        public String getFormattedValue() {
            StringBuilder str = new StringBuilder();
            for(Status s : value) {
                str.append("\n  " + s.getFormattedValue());
            }
            return str.toString();
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", ITEMSTACK);

            NBTTagList tagList = new NBTTagList();
            for(Status s : value) {
                tagList.appendTag(s.toNBT());
            }
            nbt.setTag("value", tagList);
            return nbt;
        }
    }
}
