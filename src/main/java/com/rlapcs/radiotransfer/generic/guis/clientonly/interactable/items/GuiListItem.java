package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
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

public class GuiListItem extends InteractiveGuiElement {
    private static final int[] UV = {0, 0};
    private static final int[] DIMS = {66, 15};

    private int[] XY;
    private boolean wasClicking;
    private int index;
    private AbstractTileMaterialProcessor tile;
    private GuiDumpButton dump;

    public GuiListItem(int id, int x, int y, int index, AbstractTileMaterialProcessor tile) {
        super(id, x, y, DIMS[0], DIMS[1]);
        XY = new int[2];
        XY[0] = x;
        XY[1] = y;
        wasClicking = false;
        this.index = index;
        this.tile = tile;
        if (tile.getProcessorType() == ProcessorType.ENCODER)
            dump = new GuiDumpButton(id, XY[0] + DIMS[0] - 29, XY[1] + 2);
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        boolean hoveringTop = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height / 2;
        boolean hoveringBottom = mouseX >= this.x && mouseY >= this.y + this.height / 2 && mouseX < this.x + this.width && mouseY < this.y + this.height;
        boolean flag = Mouse.isButtonDown(0);
        double scaleVal = .6875;
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(XY[0], XY[1], UV[0], UV[1], DIMS[0], DIMS[1]);
        GL11.glScaled(scaleVal, scaleVal, scaleVal);
        renderer.renderItemIntoGUI(itemStack, (int)((XY[0] + 8) / scaleVal), (int)((XY[1] + 2) / scaleVal)); //AndEffect
        GL11.glScaled(1 / scaleVal,1 / scaleVal,1 / scaleVal);
        screen.drawString(mc.fontRenderer, "×" + itemStack.getCount(), XY[0] + 20, XY[1] + 4, 0xffffff);
        if (tile.getProcessorType() == ProcessorType.ENCODER) {
            if(tile.getDumpableData() != null && tile.getHandler().size() == tile.getDumpableData().length) {
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
        } else {
            if (hoveringTop && flag && !wasClicking && index != 0)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(tile, index));
            if (hoveringBottom && flag && !wasClicking && index != tile.getHandler().size() - 1)
                ModNetworkMessages.INSTANCE.sendToServer(new MessageChangePacketPriority(tile, index + 1));
        }
        wasClicking = flag;
    }

    public int[] getPos() {
        return XY;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
