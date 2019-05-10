package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiListItem extends InteractiveGuiElement {
    private static final int[] UV = {92, 0};
    private static final int[] DIMS = {66, 20};

    private int[] XY;
    private ItemStack itemStack;

    public GuiListItem(int id, int x, int y, ItemStack itemStack) {
        super(id, x, y, DIMS[0], DIMS[1]);
        XY = new int[2];
        XY[0] = x;
        XY[1] = y;
        this.itemStack = itemStack;
    }

    public void drawItem(Minecraft mc, GuiScreen screen, RenderItem renderer) {
        mc.getTextureManager().bindTexture(ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        screen.drawTexturedModalRect(XY[0], XY[1], UV[0], UV[1], DIMS[0], DIMS[1]);
        renderer.renderItemAndEffectIntoGUI(itemStack, XY[0] + 2, XY[1] + 2);
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
