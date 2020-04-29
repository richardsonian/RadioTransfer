package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class AbstractItemProcessorGuiListItem extends AbstractGuiListItem {
    public AbstractItemProcessorGuiListItem(int id, int x, int y, int index, AbstractTileMaterialProcessor tile) {
        super(id, x, y, index, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemStack, index);
        double scaleVal = .6875;
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(XY[0], XY[1], UV[0], UV[1], DIMS[0], DIMS[1]);
        GL11.glScaled(scaleVal, scaleVal, scaleVal);
        renderer.renderItemIntoGUI(itemStack, (int)((XY[0] + 3) / scaleVal), (int)((XY[1] + 2) / scaleVal)); //AndEffect
        GL11.glScaled(1 / scaleVal,1 / scaleVal,1 / scaleVal);
        screen.drawString(mc.fontRenderer, "×" + itemStack.getCount(), XY[0] + 15, XY[1] + 4, 0xffffff);
    }
}
