package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemDecoderGuiListItem extends AbstractItemProcessorGuiListItem {
    public ItemDecoderGuiListItem(int id, int x, int y, int index, AbstractTileMaterialProcessor tile) {
        super(id, x, y, index, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemStack, index);
        AbstractTileMaterialProcessor ctile = (AbstractTileMaterialProcessor) tile;
        if (hoveringTop && flag && !wasClicking && index != 0)
            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index));
        if (hoveringBottom && flag && !wasClicking && index != ctile.getHandler().size() - 1)
            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index + 1));

        wasClicking = flag;
    }
}
