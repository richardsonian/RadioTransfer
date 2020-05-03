package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.ITransferHandler;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiDumpButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
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
    //int x, int y, int width, int height in superclass
    protected boolean wasClicking;
    protected int index;
    protected AbstractTileMachine tile;

    protected boolean hoveringTop;
    protected boolean hoveringBottom;
    protected boolean flag;

    public AbstractGuiListItem(int id, int index, CoordinateXY pos, CoordinateXY dims, AbstractTileMachine tile) {//used to be: id, x, y, index, tile
        super(id, pos.x, pos.y, dims.x, dims.y);

        this.index = index;
        this.tile = tile;

        wasClicking = false;
    }

    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, int index) {
        //Calculates hovered and clicking data
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        hoveringTop = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height / 2;
        hoveringBottom = mouseX >= this.x && mouseY >= this.y + this.height / 2 && mouseX < this.x + this.width && mouseY < this.y + this.height;
        flag = Mouse.isButtonDown(0);
    }

    public CoordinateXY getPos() {
        return new CoordinateXY(this.x, this.y);
    }
}
