package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiListItem extends InteractiveGuiElement {
    private static final int[] UV = {92, 0};
    private static final int[] DIMS = {66, 20};

    private int[] XY, offset;
    private ItemStack itemStack;
    private boolean wasClicking, isDragging, wasDragging;

    public GuiListItem(int id, int x, int y, ItemStack itemStack) {
        super(id, x, y, DIMS[0], DIMS[1]);
        offset = new int[2];
        XY = new int[2];
        XY[0] = x;
        XY[1] = y;
        wasClicking = false;
        isDragging = false;
        wasDragging = false;
        this.itemStack = itemStack;
    }

    public boolean isBeingDragged() {
        return isDragging;
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, boolean dumpable) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        boolean hoveringTop = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height / 2;
        boolean hoveringBottom = mouseX >= this.x && mouseY >= this.y + this.height / 2 && mouseX < this.x + this.width && mouseY < this.y + this.height;
        boolean flag = Mouse.isButtonDown(0);
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(XY[0], XY[1], UV[0], UV[1], DIMS[0], DIMS[1]);
        renderer.renderItemIntoGUI(itemStack, XY[0] + 2, XY[1] + 2); //AndEffect
        screen.drawString(mc.fontRenderer, "×" + itemStack.getCount(), XY[0] + 20, XY[1] + 6, 0xffffff);
        InteractiveGuiElement dump = new GuiDumpButton(id, XY[0] + DIMS[0] - 29, XY[1] + 4, dumpable);
        if (this.hovered)
            dump.drawButton(mc, mouseX, mouseY, partialTicks);

        if (hoveringTop && flag) {
        }
        if (hoveringBottom && flag) {
        }
    }

    public int[] getPos() {
        return XY;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
