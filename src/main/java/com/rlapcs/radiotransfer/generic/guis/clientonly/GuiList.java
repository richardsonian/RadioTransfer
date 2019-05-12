package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.GuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class GuiList {
    private Minecraft mc;
    private GuiScreen screen;
    private ItemPacketQueue queue;
    private GuiDraggableSliderButton bar;
    private List<GuiListItem> items;

    public GuiList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop) {
        this.mc = mc;
        this.screen = screen;
        this.queue = queue;
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            items.add(new GuiListItem(i, guiLeft + x, guiTop + y + i * 24));
        bar = new GuiDraggableSliderButton(queue.size(), 174, 26, guiLeft, guiTop, 24, 83);
    }

    public void drawList(int mouseX, int mouseY, float partialTicks, RenderItem renderer, double ratio, AbstractTileMaterialProcessor tile) {
        int start = MathHelper.clamp((int) ((queue.size() - 3) * ratio), 0, queue.size() - 3);
        List<ItemPacketQueue.PacketBuffer> itemList = queue.getAsList();
        //sendDebugMessage("bar: " + bar.getY());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (!itemList.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                int index = MathHelper.clamp(start + i, 0, queue.size() - 1);
                items.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemList.get(index).getItemStack(), index, tile);
            }
        }
    }

    public GuiDraggableSliderButton getBar() {
        return bar;
    }
}
