package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractItemProcessorGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemEncoderGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import javafx.fxml.FXML;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ItemEncoderGuiList extends AbstractItemProcessorList {
    public ItemEncoderGuiList(CoordinateXY pos, GuiScreen screen, ItemPacketQueue queue, int guiLeft, int guiTop, TileItemEncoder tile) {
        super(pos, screen, queue, guiLeft, guiTop);

        for (int i = 0; i < getNumItems(); i++)
            listItems.add(new ItemEncoderGuiListItem(i, i, new CoordinateXY(guiLeft + pos.x, guiTop + pos.y + i * (AbstractItemProcessorGuiListItem.DIMS.y + LIST_ITEM_SPACING)), tile));
    }
}
