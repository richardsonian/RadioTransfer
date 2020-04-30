package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGuiList<T> {
    protected static final int NUM_ITEMS = 4;
    private static final CoordinateXY BAR_REL_COORDS = new CoordinateXY(59, -3);

    private Minecraft mc;
    private GuiScreen screen;
    private List<T> itemList;
    private GuiDraggableSliderButton bar;
    protected List<AbstractGuiListItem> items;

    protected boolean wasClicking;
    protected boolean isScrolling;
    protected double scrollPos, scrollVal;

    public AbstractGuiList(Minecraft mc, GuiScreen screen, List<T> itemList, int x, int y, int guiLeft, int guiTop) {
        this.mc = mc;
        this.screen = screen;
        this.itemList = itemList;
        items = new ArrayList<>();
        scrollPos = 0;
        scrollVal = 0;
        /* for (int i = 0; i < NUM_ITEMS; i++) ADD TO SUBCLASSES
            items.add(new AbstractGuiListItem(i, guiLeft + x, guiTop + y + i * 18, i, tile)); */
        bar = new GuiDraggableSliderButton(itemList.size(), x + BAR_REL_COORDS.x, y + BAR_REL_COORDS.y, guiLeft, guiTop, 24, 82);
    }

    public void drawList(int mouseX, int mouseY, float partialTicks, RenderItem renderer) {
        boolean flag = Mouse.isButtonDown(0);

        if (!wasClicking && flag && bar.isMouseOver())
            isScrolling = true;
        else if (!flag)
            isScrolling = false;

        wasClicking = flag;

        int scroll = Mouse.getEventDWheel();
        boolean up = scroll > 0;
        scrollVal = up ? Math.log(Math.abs(scroll) + 1) : -Math.log(Math.abs(scroll) + 1);
        scrollPos = (bar.getY() - 24) / 59d;

        int start = MathHelper.clamp((int) ((itemList.size() - 3) * scrollPos), 0, itemList.size() - 3);
        //sendDebugMessage("bar: " + bar.getY());
        if (!itemList.isEmpty()) {
            if (itemList.size() <= NUM_ITEMS) {
                //Debug.sendDebugMessage("less");
                for (int i = 0; i < itemList.size(); i++) {
                    items.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemList.get(i), i);
                }
            } else {
                for (int i = 0; i < NUM_ITEMS; i++) {
                    int index = MathHelper.clamp(start + i, 0, itemList.size() - 1);
                    items.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemList.get(index).getItemStack(), index);
                }
            }
        }

        if (itemList.size() > 4)
            bar.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, isScrolling, scrollVal / (double) itemList.size());
    }
}
