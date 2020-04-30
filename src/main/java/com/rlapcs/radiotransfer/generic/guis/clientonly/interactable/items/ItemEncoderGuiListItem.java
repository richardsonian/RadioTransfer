package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
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
        dump = new GuiDumpButton(id, XY[0] + DIMS[0] - 36, XY[1] + 2);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, itemStack, index);
        AbstractTileMaterialProcessor ctile = (AbstractTileMaterialProcessor) tile;
        if (ctile.getDumpableData() != null && ctile.getHandler().size() == ctile.getDumpableData().length) {
            if (this.hovered)
                dump.drawButton(mc, mouseX, mouseY, partialTicks, index, ctile.getPos(), ctile.getDumpableData()[index]);
            if (hoveringTop && !dump.isMouseOver() && flag && !wasClicking && index != 0)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index));
            if (hoveringBottom && !dump.isMouseOver() && flag && !wasClicking && index != ctile.getHandler().size() - 1)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index + 1));
        }
        else {
            Debug.sendDebugMessage(TextFormatting.RED + "PACKET QUEUE DID NOT MATCH DUMP DATA");
        }

        wasClicking = flag;
    }
}
