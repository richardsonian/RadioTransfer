package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractItemProcessorGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class AbstractItemProcessorList extends AbstractGuiList{
    public static final CoordinateXY BAR_REL_COORDS = new CoordinateXY(59, -3);
    public static final int NUM_ITEMS = 4;
    public static final int LIST_ITEM_SPACING = 3;

    public AbstractItemProcessorList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop) {
        super(mc, screen, queue, x, y, guiLeft, guiTop);
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
