package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.GuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class GuiList {
    private static final int NUM_ITEMS = 4;

    private Minecraft mc;
    private GuiScreen screen;
    private ItemPacketQueue queue;
    private GuiDraggableSliderButton bar;
    private List<GuiListItem> items;

    public GuiList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop, AbstractTileMaterialProcessor tile) {
        this.mc = mc;
        this.screen = screen;
        this.queue = queue;
        items = new ArrayList<>();
        for (int i = 0; i < NUM_ITEMS; i++)
            items.add(new GuiListItem(i, guiLeft + x, guiTop + y + i * 18, i, tile));
        bar = new GuiDraggableSliderButton(queue.size(), 174, 24, guiLeft, guiTop, 24, 82);
    }

    public void drawList(int mouseX, int mouseY, float partialTicks, RenderItem renderer, double ratio) {
        int start = MathHelper.clamp((int) ((queue.size() - 3) * ratio), 0, queue.size() - 3);
        List<ItemPacketQueue.PacketBuffer> itemList = queue.getAsList();
        //sendDebugMessage("bar: " + bar.getY());
        if (!itemList.isEmpty()) {
            if (itemList.size() <= NUM_ITEMS) {
                Debug.sendDebugMessage("less");
                for (int i = 0; i < queue.size(); i++) {
                    items.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemList.get(i).getItemStack(), i);
                }
            } else {
                for (int i = 0; i < NUM_ITEMS; i++) {
                    int index = MathHelper.clamp(start + i, 0, queue.size() - 1);
                    items.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemList.get(index).getItemStack(), index);
                }
            }
        }
    }

    public GuiDraggableSliderButton getBar() {
        return bar;
    }
}
