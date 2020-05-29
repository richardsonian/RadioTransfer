package com.rlapcs.radiotransfer.generic.multiblock.data;

import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

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
        boolean flag = false;
        for(NodeStatusEntry e : entries) {
            if(nbt.hasKey("pos")) {
                if(BlockPos.fromLong(nbt.getLong("pos")).equals(e.pos)) {
                    e.readFromNBT(nbt);
                    flag = true;
                }
            }
        }
        if(!flag) {
            entries.add(new NodeStatusEntry(nbt));
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
    public List<NodeStatusEntry> getSortedEntries() {
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
}