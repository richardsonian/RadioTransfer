package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.TileItemDecoder;
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
    public ItemDecoderGuiListItem(int id, int index, CoordinateXY pos, TileItemDecoder tile) {
        super(id, index, pos, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, index);

        //Check for reordering click and sync with server (by changing server, we will automatically get an update
        TileItemDecoder ctile = (TileItemDecoder) tile;
        if (hoveringTop && index != 0) {
            if (flag && !wasClicking)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index));
            else
                topIndicator.draw(this.x, this.y - 3, mc, screen);
        }
        if (hoveringBottom && index != ctile.getHandler().size() - 1) {
            if (flag && !wasClicking)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index + 1));
            else
                bottomIndicator.draw(this.x, this.y + this.height - 1, mc, screen);
        }

        wasClicking = flag;
    }
}
