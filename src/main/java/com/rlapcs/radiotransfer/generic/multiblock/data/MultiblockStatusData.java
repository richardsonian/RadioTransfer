package com.rlapcs.radiotransfer.generic.multiblock.data;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
        boolean entryAlreadyExisted = false;
        //loop through entries to see if one with the same pos already exists
        for(NodeStatusEntry e : entries) {
            if(nbt.hasKey("pos")) {
                if(BlockPos.fromLong(nbt.getLong("pos")).equals(e.pos)) {
                    //If this is a remove message, remove the entry
                    if(nbt.hasKey("removeme") && nbt.getBoolean("removeme")) {
                        entries.remove(e);
                        Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.DARK_RED + "Removed status entry " + TextFormatting.RESET + e.getBlock().getLocalizedName());
                    }
                    //otherwise, update it with the nbt
                    else {
                        e.readFromNBT(nbt);
                        Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.BLUE + "Updated status entry " + TextFormatting.RESET + e.getBlock().getLocalizedName());
                        //Debug: Show the contents of the status entry
                        Debug.sendDebugMessage(e.toString());
                    }
                    entryAlreadyExisted = true;
                }
            }
        }
        //Add this as a new entry if not already included
        if(!entryAlreadyExisted) {
            NodeStatusEntry newEntry = new NodeStatusEntry(nbt);
            entries.add(newEntry);
            Debug.sendDebugMessage(TextFormatting.YELLOW + "[CLIENT]" + TextFormatting.GREEN + "Added new status entry " + TextFormatting.RESET + newEntry.getBlock().getLocalizedName());
            //Debug: Show the contents of the status entry
            Debug.sendDebugMessage(newEntry.toString());
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

        /**
         * Returns a string representation of the object. In general, the
         * {@code toString} method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p>
         * The {@code toString} method for class {@code Object}
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `{@code @}', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
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
