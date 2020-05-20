package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractItemProcessorGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class AbstractItemProcessorList extends AbstractGuiList{
    private static final CoordinateXY BAR_REL_COORDS = new CoordinateXY(59, -2);
    private static final int NUM_ITEMS = 4;
    protected static final int LIST_ITEM_SPACING = 3;
    private static final DimensionWidthHeight SIZE = new DimensionWidthHeight(66, 73);

    public AbstractItemProcessorList(CoordinateXY pos, GuiScreen screen, ItemPacketQueue queue, int guiLeft, int guiTop) {
        super(pos, SIZE, screen, queue, guiLeft, guiTop);
    }

    @Override
    protected void updateListItem(int listIndex, int contentIndex) {
        //set list item to itemstack from ItemPacketQueue
        ((AbstractItemProcessorGuiListItem) listItems.get(listIndex)).setItemStack(((ItemPacketQueue) listContent).peekIndex(contentIndex).getItemStack());
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
