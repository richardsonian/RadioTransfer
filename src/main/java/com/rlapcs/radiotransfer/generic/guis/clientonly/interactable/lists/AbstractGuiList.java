package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractGuiList {
    protected Minecraft mc;
    protected GuiScreen screen;
    protected GuiDraggableSliderButton scrollBar;
    protected IGuiListContent listContent;
    protected List<AbstractGuiListItem> listItems;

    protected boolean wasClicking;
    protected boolean isScrolling;
    protected double scrollPos, scrollVal;

    public AbstractGuiList(Minecraft mc, GuiScreen screen, IGuiListContent listContent, int x, int y, int guiLeft, int guiTop) {
        this.mc = mc;
        this.screen = screen;
        this.listContent = listContent;

        listItems = new ArrayList<>();
        scrollPos = 0;
        scrollVal = 0;

        scrollBar = new GuiDraggableSliderButton(listContent.size(), x + getBarRelCoords().x, y + getBarRelCoords().y, guiLeft, guiTop, 24, 82); //magic numbers?
    }

    public void drawList(int mouseX, int mouseY, float partialTicks, RenderItem renderer) {
        //establish clicking, scrolling, and mouse pos
        boolean flag = Mouse.isButtonDown(0);

        if (!wasClicking && flag && scrollBar.isMouseOver())
            isScrolling = true;
        else if (!flag)
            isScrolling = false;

        wasClicking = flag;

        int scroll = Mouse.getEventDWheel();
        boolean up = scroll > 0;
        scrollVal = up ? Math.log(Math.abs(scroll) + 1) : -Math.log(Math.abs(scroll) + 1);
        scrollPos = (scrollBar.getY() - 24) / 59d; //what are these magic numbers?

        int start = MathHelper.clamp((int) ((listContent.size() - 3) * scrollPos), 0, listContent.size() - 3); //what are these magic numbers?
        //sendDebugMessage("scrollBar: " + scrollBar.getY());

        /* Render Items */
        if (listContent.size() > 0) {
            sendDebugMessage("drawlisto");
            if (listContent.size() <= getNumItems()) {
                //Debug.sendDebugMessage("less");
                for (int i = 0; i < listContent.size(); i++) {
                    updateListItem(i, i);
                    listItems.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, i);
                }
            } else {
                for (int i = 0; i < getNumItems(); i++) {
                    int index = MathHelper.clamp(start + i, 0, listContent.size() - 1);
                    updateListItem(i, index);
                    listItems.get(i).drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, index);
                }
            }
        }

        if (listContent.size() > 4)
            scrollBar.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, isScrolling, scrollVal / (double) listContent.size());
    }

    protected abstract void updateListItem(int listIndex, int contentIndex);

    protected abstract CoordinateXY getBarRelCoords();
    protected abstract int getNumItems();
}
