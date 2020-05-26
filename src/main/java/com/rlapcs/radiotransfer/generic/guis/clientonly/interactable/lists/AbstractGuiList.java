package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.AbstractGuiListItem;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractGuiList {
    protected GuiScreen screen;
    protected GuiDraggableSliderButton scrollBar;
    protected IGuiListContent listContent;
    protected List<AbstractGuiListItem> listItems;
    protected CoordinateXY pos;
    protected DimensionWidthHeight size;
    private MoreItemsIndicator upIndicator;
    private MoreItemsIndicator downIndicator;

    protected boolean wasClicking;
    protected boolean isScrolling;
    protected double scrollVal;

    public AbstractGuiList(CoordinateXY pos, DimensionWidthHeight size, GuiScreen screen, IGuiListContent listContent, int guiLeft, int guiTop) {
        this.screen = screen;
        this.listContent = listContent;
        this.pos = pos.addTo(new CoordinateXY(guiLeft, guiTop)); // GO BACK AND STREAMLINE DAMN YOU
        this.size = size;

        listItems = new ArrayList<>();
        scrollVal = 0;

        scrollBar = new GuiDraggableSliderButton(this.pos.addTo(getBarRelCoords()));
        upIndicator = new MoreItemsIndicator(true, this.pos.addTo(new CoordinateXY((size.width - 8) / 2, -5))); // magic scrollbar width
        downIndicator = new MoreItemsIndicator(false, this.pos.addTo(new CoordinateXY((size.width - 8) / 2, size.height - 4)));
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

        int start = MathHelper.clamp((int) ((listContent.size() - 3) * scrollBar.getScrollPos()), 0, listContent.size() - 3);
        //sendDebugMessage("scrollBar: " + scrollBar.getScrollPos());

        /* Render Items */
        if (listContent.size() > 0) {
            //sendDebugMessage("drawlisto");
            if (listContent.size() <= getNumItems()) {
                //Debug.sendDebugMessage("less");
                for (int i = 0; i < listContent.size(); i++) {
                    updateListItem(i, i);
                    listItems.get(i).drawItem(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks, screen, renderer, i);
                }
            } else {
                for (int i = 0; i < getNumItems(); i++) {
                    int index = MathHelper.clamp(start + i, 0, listContent.size() - 1);
                    updateListItem(i, index);
                    listItems.get(i).drawItem(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks, screen, renderer, index);
                }
            }
        }

        if (listContent.size() > 4) {
            scrollBar.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, isScrolling, scrollVal / (double) listContent.size());
            if (start != 0)
                upIndicator.draw();
            if (start + 3 < listContent.size() - 1)
                downIndicator.draw();
        }
    }

    protected abstract void updateListItem(int listIndex, int contentIndex);

    protected abstract CoordinateXY getBarRelCoords();
    protected abstract int getNumItems();

    class MoreItemsIndicator {
        private static final int BOUNCE_RANGE = 1;
        private static final double SPEED = 0.15;
        private boolean up;
        private double tick;
        private CoordinateXY pos;

        MoreItemsIndicator(boolean up, CoordinateXY pos) {
            this.up = up;
            this.pos = pos;
            this.tick = 0;
        }

        void draw() {
            // calculate y pos

            tick += SPEED;
            tick = tick % (2 * Math.PI);

            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            screen.drawTexturedModalRect(pos.x - 3, (int) (pos.y + BOUNCE_RANGE * Math.sin(tick)), up ? 76 : 70, 26, 6, 8);

            RenderHelper.enableStandardItemLighting();
        }
    }
}
