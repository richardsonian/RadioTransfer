package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class PowerSupplyListItem extends AbstractGuiListItem {
    public PowerSupplyListItem(int id, int x, int y, int index, TilePowerSupply tile) {
        super(id, x, y, index, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemStack, index);
        renderWithItemStack(mc, screen, renderer, itemStack);
        screen.drawString(mc.fontRenderer, "500 FE", XY[0] + 15, XY[1] + 4, 0xffffff);

        wasClicking = flag;
    }
}
