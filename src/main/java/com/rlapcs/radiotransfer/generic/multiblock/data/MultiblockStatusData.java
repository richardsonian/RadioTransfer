package com.rlapcs.radiotransfer.generic.multiblock.data;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

import static net.minecraft.util.text.TextFormatting.*;

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

    public void readNodesFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("node_status_list")) {
            NBTTagList tagList = nbt.getTagList("node_status_list", Constants.NBT.TAG_COMPOUND);
            Debug.sendDebugMessage(ITALIC.toString() + DARK_GRAY + "...adding list of " + tagList.tagCount() + " nodes");
            for(int i = 0; i < tagList.tagCount(); i++) {
                Debug.sendDebugMessage(ITALIC.toString() + TextFormatting.GRAY + "...adding node " + i);
                NBTTagCompound nodeNBT = (NBTTagCompound) tagList.get(i);
                this.readNodeFromNBT(nodeNBT);
            }
            onNodeListChanged();
        }
        else {
            Debug.sendDebugMessage(RED + "[WARNING]" + RESET + " nbt at MultiblockStatusData#readNodesFromNBT() does not contain node list.");
        }
    }
    public void readNodeFromNBT(NBTTagCompound nbt) {
        Debug.sendDebugMessage(String.format("%s%s...reading node from NBT in %s[msd]", ITALIC, DARK_GRAY, GRAY));
        boolean entryAlreadyExisted = false;
        //loop through entries to see if one with the same pos already exists
        for(NodeStatusEntry e : entries) {
            if(nbt.hasKey("pos")) {
                if(BlockPos.fromLong(nbt.getLong("pos")).equals(e.pos)) {
                    Debug.sendDebugMessage(DARK_GRAY.toString() + ITALIC + "...found existing entry of same pos");
                    //If this is a remove message, remove the entry
                    if(nbt.hasKey("removeme") && nbt.getBoolean("removeme")) {
                        Debug.sendDebugMessage(DARK_GRAY.toString() + ITALIC + "...is remove key");
                        entries.remove(e);
                        Debug.sendDebugMessage(YELLOW + "[CLIENT]" + DARK_RED + "Removed status entry " + RESET + e.getBlock().getLocalizedName());
                        onNodeListChanged();
                    }
                    //otherwise, update it with the nbt
                    else {
                        Debug.sendDebugMessage(DARK_GRAY.toString() + ITALIC + "...isn't remove key. Just reading normally.");
                        e.readFromNBT(nbt);
                        Debug.sendDebugMessage(YELLOW + "[CLIENT]" + BLUE + "Updated status entry " + RESET + e.getBlock().getLocalizedName());
                        //Debug: Show the contents of the status entry
                        //Debug.sendDebugMessage(e.toString());
                    }
                    entryAlreadyExisted = true;
                }
            }
        }
        //Add this as a new entry if not already included
        if(!entryAlreadyExisted) {
            Debug.sendDebugMessage(DARK_GRAY.toString() + ITALIC + "...no existing entry found. Creating new one.");
            NodeStatusEntry newEntry = new NodeStatusEntry(nbt);
            Debug.sendDebugMessage(GRAY.toString() + ITALIC + "...Constructed new object");
            entries.add(newEntry);
            Debug.sendDebugMessage(GRAY.toString() + ITALIC + "...added object");
            Debug.sendDebugMessage(YELLOW + "[CLIENT]" + GREEN + "Added new status entry " + RESET + newEntry.getBlock().getLocalizedName());
            onNodeListChanged();
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

    /**
     * Override this to get a hook for when a node is added or removed
     */
    public void onNodeListChanged() {

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
            //Debug.sendDebugMessage(GRAY.toString() + ITALIC + "....inside constructor");
            this.readFromNBT(nbt);
            //Debug.sendDebugMessage(GRAY.toString() + ITALIC + "....finished constructor");
        }
        public void readFromNBT(NBTTagCompound nbt) {
            Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....parsing node NBT");
            if(nbt.hasKey("pos")) {
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....has pos key");
                pos = BlockPos.fromLong(nbt.getLong("pos"));
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....got pos key");
            }
            if(nbt.hasKey("block")) {
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....has block key");
                block = Block.getBlockFromName(nbt.getString("block"));
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....got block key");
            }
            if(nbt.hasKey("statuses")) {
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....has statuses key");
                statuses = new ArrayList<>();
                NBTTagList tagList = nbt.getTagList("statuses", Constants.NBT.TAG_COMPOUND);
                //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....got statuses key. Parsing");
                for(int i = 0; i < tagList.tagCount(); i++) {
                    //Debug.sendDebugMessage(WHITE.toString() + ITALIC + ".... - reading status " + i);
                    statuses.add(Status.fromNBT((NBTTagCompound) tagList.get(i)));
                }
            }
            //Debug.sendDebugMessage(WHITE.toString() + ITALIC + "....finished node NBT parsing");
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
            str.append(UNDERLINE + "NodeStatus - " + block.getLocalizedName());
            str.append(String.format("%s\nPOS:[%d, %d, %d]", RESET, pos.getX(), pos.getY(), pos.getZ()));
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
        public static final int INT_WITH_UNITS = 7;
        public static final int DOUBLE_WITH_UNITS = 8;
        public static final int FRACTION_WITH_UNITS = 9;
        public static final int UPGRADECARD = 10;

        //Data
        protected String key;
        protected T value;

        public Status(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public static Status fromNBT(NBTTagCompound nbt) {
            Debug.sendDebugMessage(GRAY.toString() + ITALIC + "   ...creating new status from NBT");
            if(nbt.hasKey("key") && nbt.hasKey("type") && nbt.hasKey("value")) {
                String key = nbt.getString("key");
                int type = nbt.getInteger("type");
                Debug.sendDebugMessage(WHITE.toString() + ITALIC + "      [key: " + key + " ; type: " + type + "]");
                switch(type) {
                    case INT: return new StatusInt(key, nbt.getInteger("value"));
                    case DOUBLE: return new StatusDouble(key, nbt.getDouble("value"));
                    case BOOL: return new StatusBool(key, nbt.getBoolean("value"));
                    case STRING: return new StatusString(key, nbt.getString("value"));
                    case FRACTION: return new StatusFraction(key, nbt.getCompoundTag("value"));
                    case ITEMSTACK: return new StatusItemStack(key, nbt.getCompoundTag("value"));
                    case LIST: return new StatusList(key, nbt.getTagList("value", Constants.NBT.TAG_COMPOUND));
                    case INT_WITH_UNITS: return new StatusInt.WithUnits(key, nbt);
                    case DOUBLE_WITH_UNITS: return new StatusDouble.WithUnits(key, nbt);
                    case FRACTION_WITH_UNITS: return new StatusFraction.WithUnits(key, nbt);
                    case UPGRADECARD: return new StatusUpgradeCard(key, nbt.getInteger("value"));
                    default: {
                        Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " status type not found.");
                        throw new IllegalArgumentException("Tag \"type\" contains unrecognizable content.");
                    }
                }
            }
            //if above didn't return
            Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Status NBT does not contain correct tags.");
            throw new IllegalArgumentException("NBT at Status.fromNBT does not contain required tags.");
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
        public static class WithUnits extends StatusInt {
            private String units;
            public WithUnits(String key, Integer value, String units) {
                super(key, value);
                this.units = units;
            }
            public WithUnits(String key, NBTTagCompound nbt) {
                super(key, null);
                if(nbt.hasKey("value") && nbt.hasKey("units")) {
                    value = nbt.getInteger("value");
                    units = nbt.getString("units");
                }
                else {
                    Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Failed to read NBT when creating StatusInt.WithUnits");
                    throw new IllegalArgumentException("Required tags not present.");
                } //error catching
            }
            @Override
            public String getFormattedValue() {
                return value + units;
            }
            @Override
            public NBTTagCompound toNBT() {
                NBTTagCompound nbt = super.toNBT();
                nbt.setInteger("type", INT_WITH_UNITS);
                nbt.setString("units", units);
                return nbt;
            }
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

        public static class WithUnits extends StatusDouble {
            private String units;
            public WithUnits(String key, Double value, String units) {
                super(key, value);
                this.units = units;
            }
            public WithUnits(String key, NBTTagCompound nbt) {
                super(key, null);
                if(nbt.hasKey("value") && nbt.hasKey("units")) {
                    value = nbt.getDouble("value");
                    units = nbt.getString("units");
                }
                else {
                    Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Failed to read NBT when creating StatusDouble.WithUnits");
                    throw new IllegalArgumentException("Required tags not present.");
                } //error catching
            }
            @Override
            public String getFormattedValue() {
                return String.format("%.2f%s", value, units);
            }
            @Override
            public NBTTagCompound toNBT() {
                NBTTagCompound nbt = super.toNBT();
                nbt.setInteger("type", DOUBLE_WITH_UNITS);
                nbt.setString("units", units);
                return nbt;
            }
        }
    }
    public static class StatusBool extends Status<Boolean> {
        public StatusBool(String key, Boolean value) {
            super(key, value);
        }
        @Override
        public String getFormattedValue() {
            return (value ? (GREEN + "✔") : (RED + "✘"));
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
                if(nbt.hasKey("numerator") && nbt.hasKey("denominator")) {
                    numerator = nbt.getInteger("numerator");
                    denominator = nbt.getInteger("denominator");
                }
                else {
                    Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Failed to read NBT when deserializing StatusFraction.Fraction");
                    throw new IllegalArgumentException("Required tags not present.");
                } //error catching
            }

            @Override
            public String toString() {
                return (numerator + " / " + denominator);
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

        public static class WithUnits extends StatusFraction {
            private String units;
            public WithUnits(String key, Fraction value, String units) {
                super(key, value);
                this.units = units;
            }
            public WithUnits(String key, int numerator, int denominator, String units) {
                super(key, numerator, denominator);
                this.units = units;
            }
            public WithUnits(String key, NBTTagCompound nbt) {
                super(key, nbt.getCompoundTag("value"));
                if(nbt.hasKey("units")) {
                    units = nbt.getString("units");
                }
                else {
                    Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Failed to read NBT when creating StatusFraction.WithUnits");
                    throw new IllegalArgumentException("Required tags not present.");
                } //error catching
            }
            @Override
            public String getFormattedValue() {
                return value + units;
            }
            @Override
            public NBTTagCompound toNBT() {
                NBTTagCompound nbt = super.toNBT();
                nbt.setInteger("type", FRACTION_WITH_UNITS);
                nbt.setString("units", units);
                return nbt;
            }
        }
    }

    /**
     * Key: String
     * Value: ItemStack
     * Ex: "Power Item": [ItemStack]
     */
    public static class StatusItemStack extends Status<ItemStack> {
        boolean shouldRenderQuantity;
        public StatusItemStack(String key, ItemStack value, boolean renderQuantity) {
            super(key, value);
            this.shouldRenderQuantity = renderQuantity;
        }
        public StatusItemStack(String key, ItemStack value) {
            this(key, value, true);
        }
        public StatusItemStack(String key, NBTTagCompound valueNBT) {
            this(key, new ItemStack(valueNBT));
            if(valueNBT.hasKey("_status_renderQuantity")) {
                this.shouldRenderQuantity = valueNBT.getBoolean("_status_renderQuantity");
            }
            else {
                Debug.sendDebugMessage(RED + "[ERROR]" + RESET + " Failed to read NBT when creating StatusItemStack");
                throw new IllegalArgumentException("Required tags not present.");
            } //error catching
        }
        @Override
        public String getFormattedValue() {
            if (!value.isEmpty()) {
                if(shouldRenderQuantity) {
                    return value.getDisplayName() + " ×" + value.getCount();
                }
                else {
                    return value.getDisplayName();
                }
            }
            else {
                return RED + "✘";
            }
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", ITEMSTACK);

            NBTTagCompound valueNBT = value.serializeNBT();
            valueNBT.setBoolean("_status_renderQuantity", shouldRenderQuantity); //sneak this in the ItemStack NBT so we can use it
            nbt.setTag("value", valueNBT);

            return nbt;
        }
        public int getQuantity() {
            return value.getCount();
        }
        public boolean shouldRenderQuantity() {
            return shouldRenderQuantity;
        }
    }

    /**
     * Unlike StatusItemStack, this stores an Item as key and a quantity as a value.
     * In the backend, this is actually stored as a modified StatusInt.
     * The Item key is stored as a string key, the Int value is the quantity.
     *
     * A bit of a hack, but should work.
     */
    public static class StatusUpgradeCard extends StatusInt {
        public StatusUpgradeCard(String itemName, int quantity) {
            super(itemName, quantity);
        }
        public StatusUpgradeCard(Item upgradeItem, int quantity) {
            this(Item.REGISTRY.getNameForObject(upgradeItem).toString(), quantity);
            Debug.sendDebugMessage("Created UpgradeStatus Card with key: " + key);
        }

        @Override
        public String getFormattedValue() {
            if(getQuantity() > 0) {
                return "×" + getQuantity();
            }
            else {
                return RESET + ": " + RED + "✘";
            }
        }

        @Override
        public String toString() {
            return getItemStack().getDisplayName() + getFormattedValue();
        }

        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", UPGRADECARD);
            return nbt;
        }

        public Item getItem() {
            return Item.getByNameOrId(this.key);
        }
        public int getQuantity() {
            return this.getValue();
        }

        /**
         * Gets ItemStack for render. If actual quantity is 0, sets it to 1 to prevent it from
         * @return the ItemStack
         */
        public ItemStack getItemStack() {
            return new ItemStack(getItem(), getQuantity() > 0 ? getQuantity() : 1);
        }
    }

    public static class StatusList extends Status<List<Status>> {
        public StatusList(String key, List<Status> value) {
            super(key, value);
        }
        public StatusList(String key, NBTTagList valueNBT) {
            this(key, new ArrayList<>());
            Debug.sendDebugMessage(GRAY.toString() + ITALIC + "      ...creating StatusList from NBT (count: " + valueNBT.tagCount() + ")");
            for(int i = 0; i < valueNBT.tagCount(); i++) {
                Debug.sendDebugMessage(WHITE.toString() + ITALIC + "[LIST TAG " + i + "]:");
                this.value.add(Status.fromNBT((NBTTagCompound) valueNBT.get(i)));
            }
        }
        @Override
        public String getFormattedValue() {
            StringBuilder str = new StringBuilder();
            for(Status s : value) {
                str.append("\n  " + RESET + s.toString());
            }
            return str.toString();
        }
        @Override
        public NBTTagCompound toNBT() {
            NBTTagCompound nbt = super.toNBT();
            nbt.setInteger("type", LIST);

            NBTTagList tagList = new NBTTagList();
            for(Status s : value) {
                tagList.appendTag(s.toNBT());
            }
            nbt.setTag("value", tagList);
            return nbt;
        }
    }
}
