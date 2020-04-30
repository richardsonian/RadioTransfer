package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.ITransferHandler;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
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

public abstract class AbstractGuiListItem extends InteractiveGuiElement {
    protected static int[] UV = {0, 0};
    protected static int[] DIMS = {66, 15};

    protected int[] XY;
    protected boolean wasClicking;
    protected int index;
    protected AbstractTileMachine tile;
    protected GuiDumpButton dump;

    protected boolean hoveringTop;
    protected boolean hoveringBottom;
    protected boolean flag;

    public AbstractGuiListItem(int id, int x, int y, int index, AbstractTileMachine tile) {
        super(id, x, y, DIMS[0], DIMS[1]);
        XY = new int[2];
        XY[0] = x;
        XY[1] = y;
        wasClicking = false;
        this.index = index;
        this.tile = tile;
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        hoveringTop = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height / 2;
        hoveringBottom = mouseX >= this.x && mouseY >= this.y + this.height / 2 && mouseX < this.x + this.width && mouseY < this.y + this.height;
        flag = Mouse.isButtonDown(0);
    }

    protected void renderWithItemStack(Minecraft mc, GuiScreen screen, RenderItem renderer, ItemStack itemStack) {
        double scaleVal = .6875;
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(XY[0], XY[1], UV[0], UV[1], DIMS[0], DIMS[1]);
        GL11.glScaled(scaleVal, scaleVal, scaleVal);
        renderer.renderItemIntoGUI(itemStack, (int)((XY[0] + 3) / scaleVal), (int)((XY[1] + 2) / scaleVal));
        GL11.glScaled(1 / scaleVal,1 / scaleVal,1 / scaleVal);
    }

    public int[] getPos() {
        return XY;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}