package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemEncoderGuiListItem;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import javafx.fxml.FXML;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ItemEncoderGuiList extends AbstractGuiList {
    public ItemEncoderGuiList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop, TileItemEncoder tile) {
        super(mc, screen, queue, x, y, guiLeft, guiTop);
        for (int i = 0; i < NUM_ITEMS; i++)
            items.add(new ItemEncoderGuiListItem(i, guiLeft + x, guiTop + y + i * 18, i, tile));
    }
}
