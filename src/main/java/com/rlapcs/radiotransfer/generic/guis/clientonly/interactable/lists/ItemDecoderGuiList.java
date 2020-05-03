package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractItemProcessorGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import static com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractItemProcessorList.LIST_ITEM_SPACING;
import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class ItemDecoderGuiList extends AbstractItemProcessorList {

    public ItemDecoderGuiList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop, TileItemDecoder tile) {
        super(mc, screen, queue, x, y, guiLeft, guiTop);

        for (int i = 0; i < getNumItems(); i++)
            listItems.add(new ItemDecoderGuiListItem(i, i, new CoordinateXY(guiLeft + x, guiTop + y + i * (AbstractItemProcessorGuiListItem.DIMS.y + LIST_ITEM_SPACING)), tile));
    }


}
