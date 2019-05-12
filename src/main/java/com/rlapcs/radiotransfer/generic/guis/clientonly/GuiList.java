package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.GuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class GuiList {
    private ItemPacketQueue queue;
    private int[] XY;
    private GuiDraggableSliderButton bar;

    public GuiList(ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop) {
        this.queue = queue;

        XY = new int[2];
        XY[0] = guiLeft + x;
        XY[1] = guiTop + y;
        bar = new GuiDraggableSliderButton(queue.size(),  174,  26, guiLeft, guiTop, 24, 83);
    }

    public void drawList(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, double ratio, boolean dumpable) {
        int start = MathHelper.clamp((int) ((queue.size() - 3) * ratio), 0, queue.size() - 3);
        List<ItemPacketQueue.PacketBuffer> itemList = queue.getAsList();
        //sendDebugMessage("bar: " + bar.getY());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if(!itemList.isEmpty()) {
            for (int i = 0; i < 3; i++)
                (new GuiListItem(i, XY[0], XY[1] + i * 24, itemList.get(MathHelper.clamp(start + i, 0, queue.size() - 1)).getItemStack())).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, dumpable);
        }
    }

    public GuiDraggableSliderButton getBar() {
        return bar;
    }
}
