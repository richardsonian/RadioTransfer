package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.ItemDecoderGuiListItem;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class ItemDecoderGuiList extends AbstractGuiList {
    public ItemDecoderGuiList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop, TileItemDecoder tile) {
        super(mc, screen, queue, x, y, guiLeft, guiTop);

        for (int i = 0; i < NUM_ITEMS; i++)
            items.add(new ItemDecoderGuiListItem(i, guiLeft + x, guiTop + y + i * 18, i, tile));
    }
}
