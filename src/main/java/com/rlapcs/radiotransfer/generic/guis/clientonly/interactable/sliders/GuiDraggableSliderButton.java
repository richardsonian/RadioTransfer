package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiDraggableSliderButton extends InteractiveGuiElement {
    private static final CoordinateUV UV = new CoordinateUV(0, 15);
    private static final CoordinateXY DIMS = new CoordinateXY(8, 15);

    private CoordinateXY pos;
    private int guiLeft, guiTop, mouseOffset, min, max;
    private boolean wasDragging;

    public GuiDraggableSliderButton(int id, int x, int y, int guiLeft, int guiTop, int min, int max) {
        super(id, x, y, DIMS.x, DIMS.y);

        pos = new CoordinateXY(guiLeft + x, guiTop + y);
        this.min = min;
        this.max = max;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        this.wasDragging = false;
    }

    public int getY() {
        return pos.y - guiTop;
    }

    @Override
    protected CoordinateUV getUV() {
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
            mc.getTextureManager().bindTexture(ModConstants.ICONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //sendDebugMessage("pressed: " + mousePressed(mc, mouseX, mouseY));
            //if (dragging)
            //    XY[1] = MathHelper.clamp(mouseY, guiTop + minMax[0] + mouseOffset, guiTop + minMax[1] + mouseOffset);
            if (!wasDragging && dragging)
                mouseOffset = mouseY - pos.y;
            if (dragging)
                pos.y = mouseY - mouseOffset;
            else if (!wasDragging)
                mouseOffset = 0;
            //sendDebugMessage(wasDragging + " " + dragging + " " + mouseOffset + "");
            //XY[1] = 2 * XY[1] - mouseY;
            boolean up = scroll > 0;
            int scrollChange = up ? (int) Math.ceil(Math.abs(scroll)) : - (int) Math.ceil(Math.abs(scroll));
            pos.y = MathHelper.clamp(pos.y - scrollChange, guiTop + min, guiTop + max);
            this.drawTexturedModalRect(pos.x, pos.y, getUV().u, getUV().v, this.width, this.height);
            wasDragging = dragging;
            this.hovered = mouseX >= pos.x && mouseY >= pos.y && mouseX < pos.x + this.width && mouseY < pos.y + this.height;
        }
    }
}
