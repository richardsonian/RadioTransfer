package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ItemEncoderGuiListItem extends AbstractItemProcessorGuiListItem {
    public ItemEncoderGuiListItem(int id, int x, int y, int index, AbstractTileMaterialProcessor tile) {
        super(id, x, y, index, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemStack, index);
        if (tile.getDumpableData() != null && tile.getHandler().size() == tile.getDumpableData().length) {
            if (this.hovered)
                dump.drawButton(mc, mouseX, mouseY, partialTicks, index, tile.getPos(), tile.getDumpableData()[index]);
            if (hoveringTop && !dump.isMouseOver() && flag && !wasClicking && index != 0)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(tile, index));
            if (hoveringBottom && !dump.isMouseOver() && flag && !wasClicking && index != tile.getHandler().size() - 1)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(tile, index + 1));
        }
        else {
            Debug.sendDebugMessage(TextFormatting.RED + "PACKET QUEUE DID NOT MATCH DUMP DATA");
        }

        wasClicking = flag;
    }
}
