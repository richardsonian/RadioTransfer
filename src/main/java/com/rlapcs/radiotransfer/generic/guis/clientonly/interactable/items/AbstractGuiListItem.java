package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.ITransferHandler;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
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
    protected static final int[] UV = {0, 0};
    protected static final int[] DIMS = {66, 15};

    protected int[] XY;
    protected boolean wasClicking;
    protected int index;
    protected AbstractTileMaterialProcessor tile;
    protected GuiDumpButton dump;

    protected boolean hoveringTop;
    protected boolean hoveringBottom;
    protected boolean flag;

    public AbstractGuiListItem(int id, int x, int y, int index, AbstractTileMaterialProcessor tile) {
        super(id, x, y, DIMS[0], DIMS[1]);
        XY = new int[2];
        XY[0] = x;
        XY[1] = y;
        wasClicking = false;
        this.index = index;
        this.tile = tile;
        if (tile.getProcessorType() == ProcessorType.ENCODER)
            dump = new GuiDumpButton(id, XY[0] + DIMS[0] - 36, XY[1] + 2);
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, ItemStack itemStack, int index) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        hoveringTop = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height / 2;
        hoveringBottom = mouseX >= this.x && mouseY >= this.y + this.height / 2 && mouseX < this.x + this.width && mouseY < this.y + this.height;
        flag = Mouse.isButtonDown(0);
    }

    public int[] getPos() {
        return XY;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
