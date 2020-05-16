package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractItemProcessorGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.PowerSupplyListItem;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockPowerUsageData;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class PowerSupplyList extends AbstractGuiList {
    public static final CoordinateXY BAR_REL_COORDS = new CoordinateXY(70, -3); //values copied from processor, tbd
    public static final int NUM_ITEMS = 4;//values copied from processor, tbd
    public static final int LIST_ITEM_SPACING = 3;//values copied from processor, tbd

    public PowerSupplyList(Minecraft mc, GuiScreen screen, MultiblockPowerUsageData powerUsageData, int x, int y, int guiLeft, int guiTop, TilePowerSupply tile) {
        super(mc, screen, powerUsageData, x, y, guiLeft, guiTop);

        for (int i = 0; i < getNumItems(); i++)
            listItems.add(new PowerSupplyListItem(i, i, new CoordinateXY(guiLeft + x, guiTop + y + i * (PowerSupplyListItem.DIMS.y + LIST_ITEM_SPACING)), tile));
    }

    @Override
    protected void updateListItem(int listIndex, int contentIndex) {
        //sendDebugMessage("updateListItem");
        MultiblockPowerUsageData.PowerUsageEntry content = ((MultiblockPowerUsageData) listContent).getSortedEntries().get(contentIndex);
        ((PowerSupplyListItem) listItems.get(listIndex)).setPowerData(content);
    }

    @Override
    protected CoordinateXY getBarRelCoords() {
        return BAR_REL_COORDS;
    }
    @Override
    protected int getNumItems() {
        return NUM_ITEMS;
    }
}
