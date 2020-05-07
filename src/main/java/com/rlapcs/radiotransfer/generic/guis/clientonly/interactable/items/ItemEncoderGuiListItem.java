package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.text.TextFormatting;

public class ItemEncoderGuiListItem extends AbstractItemProcessorGuiListItem {
    public static CoordinateXY DUMP_REL_POS = new CoordinateXY(DIMS.x - 36, 2);

    protected GuiDumpButton dumpButton;

    public ItemEncoderGuiListItem(int id, int index, CoordinateXY pos, TileItemEncoder tile) {
        super(id, index, pos, tile);

        dumpButton = new GuiDumpButton(id, this.x + DUMP_REL_POS.x, this.y + DUMP_REL_POS.y);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer,  int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, index);
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();

        TileItemEncoder ctile = (TileItemEncoder) tile;
        if (ctile.getDumpableData() != null && ctile.getHandler().size() == ctile.getDumpableData().length) {
            if (this.hovered)
                dumpButton.drawButton(mc, mouseX, mouseY, partialTicks, index, ctile.getPos(), ctile.getDumpableData()[index]);
        }
        else {
            Debug.sendDebugMessage(TextFormatting.RED + "PACKET QUEUE DID NOT MATCH DUMP DATA");
        }
        RenderHelper.enableStandardItemLighting();

        //Check for reordering click and sync with server (by changing server, we will automatically get an update
        if (hoveringTop && index != 0 && !dumpButton.isHighlighted()) {
            if (flag && !wasClicking)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index));
            else
                topIndicator.draw(this.x, this.y - 3, mc, screen);
        }
        if (hoveringBottom && index != ctile.getHandler().size() - 1 && !dumpButton.isHighlighted()) {
            if (flag && !wasClicking)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(ctile, index + 1));
            else
                bottomIndicator.draw(this.x, this.y + this.height - 1, mc, screen);
        }

        wasClicking = flag;
    }
}
