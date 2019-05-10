package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public class GuiDraggableSliderButton extends InteractiveGuiElement {
    private static final int[] UV = {0, 15};
    private static final int[] DIMS = {8, 15};

    private static int[] XY, minMax;

    private int guiLeft, guiTop, mouseOffset;
    private boolean wasDragging;

    public GuiDraggableSliderButton(int id, int x, int y, int guiLeft, int guiTop, int min, int max) {
        super(id, x, y, DIMS[0], DIMS[1]);
        XY = new int[2];
        minMax = new int[2];
        XY[0] = guiLeft + x;
        XY[1] = guiTop + y;
        minMax[0] = min;
        minMax[1] = max;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        wasDragging = false;
    }

    public int getY() {
        return XY[1] - guiTop;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }

    /*@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        dragging = this.isMouseOver();
        if (dragging)
            mouseOffset = (mouseY - XY[1]);
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        sendDebugMessage("release");
        dragging = false;
    }*/

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, boolean dragging, double scroll) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(ICONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //sendDebugMessage("pressed: " + mousePressed(mc, mouseX, mouseY));
            //if (dragging)
            //    XY[1] = MathHelper.clamp(mouseY, guiTop + minMax[0] + mouseOffset, guiTop + minMax[1] + mouseOffset);
            if (!wasDragging && dragging)
                mouseOffset = (mouseY - XY[1]);
            if (dragging)
                XY[1] = mouseY - mouseOffset;
            else if (!wasDragging)
                mouseOffset = 0;
            //sendDebugMessage(wasDragging + " " + dragging + " " + mouseOffset + "");
            //XY[1] = 2 * XY[1] - mouseY;
            boolean up = scroll > 0;
            int scrollChange = up ? (int) Math.ceil(Math.abs(scroll)) : - (int) Math.ceil(Math.abs(scroll));
            XY[1] = MathHelper.clamp(XY[1] - scrollChange, guiTop + minMax[0], guiTop + minMax[1]);
            this.drawTexturedModalRect(XY[0], XY[1], getUV()[0], getUV()[1], this.width, this.height);
            wasDragging = dragging;
            this.hovered = mouseX >= XY[0] && mouseY >= XY[1] && mouseX < XY[0] + this.width && mouseY < XY[1] + this.height;
        }
    }
}
