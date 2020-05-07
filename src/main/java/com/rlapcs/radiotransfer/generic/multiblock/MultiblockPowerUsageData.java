package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.IGuiListContent;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;
import scala.collection.immutable.List;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiblockPowerUsageData implements IGuiListContent, INBTSerializable {
    private Set<PowerUsageEntry> entries;

    public MultiblockPowerUsageData() {
        entries = new HashSet<>();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public NBTBase serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {

    }

/*~~~~~~~~~~~~~~~~~ENTRY SUBCLASS~~~~~~~~~~~~~~~~~~~~~~*/
    public static class PowerUsageEntry {
        public Class<? extends AbstractTileMultiblockNode> nodeClass;

        public Instant lastUpdated;

        public int basePowerPerTick;
        public Set<UpgradeCardPowerEntry> upgradeCardConstantCosts;
        public int effectivePowerPerTick;

        public int basePowerPerProcess;
        public Set<UpgradeCardPowerEntry> upgradeCardProcessCosts;
        public int effectivePowerPerProcess;
        public int processRate;
        public int averageProcessPowerPerTick;

        public int totalPowerPerTick;

        public PowerUsageEntry(){}
        public PowerUsageEntry(AbstractTileMultiblockNode te) {
            this.update(te);
            lastUpdated = Instant.now();
        }

        public void update(AbstractTileMultiblockNode te) {
            basePowerPerTick = te.getBasePowerPerTick();
            upgradeCardConstantCosts = new HashSet<>();
            te.getConstantPowerContributingUpgrades().forEach((u) -> upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardConstantPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
            effectivePowerPerTick = te.getPowerPerTick();

            basePowerPerProcess = te.getBasePowerPerProcess();
            upgradeCardProcessCosts = new HashSet<>();
            te.getProcessPowerContributingUpgrades().forEach((u) -> upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardProcessPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
            effectivePowerPerProcess = te.getPowerPerProcess();
            processRate = te.getAverageProcessesRate();
            averageProcessPowerPerTick = te.getAverageProcessPowerPerTick();

            totalPowerPerTick = effectivePowerPerTick + effectivePowerPerProcess;
        }

        public static class UpgradeCardPowerEntry {
            public Item item;
            public int quantity;
            public int cost;
            public int total;

            public UpgradeCardPowerEntry() {
                this(null, 0, 0);
            }
            public UpgradeCardPowerEntry(Item item, int quantity, int cost) {
                this.item = item;
                this.quantity = quantity;
                this.cost = cost;

                this.total = quantity * cost;
            }
        }
    }
}
