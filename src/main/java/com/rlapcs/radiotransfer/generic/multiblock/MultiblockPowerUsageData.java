package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.IGuiListContent;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import scala.collection.immutable.List;

public class MultiblockPowerUsageData implements IGuiListContent {
    private List<PowerUsageEntry> entries;

    @Override
    public int size() {
        return entries.size();
    }

    public static class PowerUsageEntry {
        private AbstractTileMultiblockNode tile;


        public PowerUsageEntry() {

        }

        public ItemStack getMachineRenderItem(){
            return null;
        }
    }
}
