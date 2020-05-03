package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class AbstractItemProcessorGuiListItem extends AbstractGuiListItem {
    //these variables are static, as they are the same in all Item Processor GUIs (save memory)
    public static final CoordinateUV UV = new CoordinateUV(0,0); //has getter method to be accessed by superclass if needed
    public static final CoordinateXY DIMS = new CoordinateXY(66, 15);
    public static final CoordinateXY ITEM_REL_POS = new CoordinateXY(3, 2);
    public static final double ITEM_SCALE = .6875;
    public static final CoordinateXY ITEM_QUANTITY_REL_POS = new CoordinateXY(15,4);

    //instance variables
    private ItemStack itemStack;

    public AbstractItemProcessorGuiListItem(int id, int index, CoordinateXY pos, AbstractTileMachine tile) {
        super(id, index, pos, DIMS, tile);
    }

    @Override
    public void drawItem(Minecraft mc, int mouseX, int mouseY, float partialTicks, GuiScreen screen, RenderItem renderer, int index) {
        super.drawItem(mc, mouseX, mouseY, partialTicks, screen, renderer, index);

        //draw listItem texture
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(this.x, this.y, getUV().u, getUV().v, this.width, this.height);

        //Render Item
        GL11.glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        renderer.renderItemIntoGUI(this.itemStack, (int)((this.x + ITEM_REL_POS.x) / ITEM_SCALE), (int)((this.y + ITEM_REL_POS.y) / ITEM_SCALE));
        GL11.glScaled(1 / ITEM_SCALE,1 / ITEM_SCALE,1 / ITEM_SCALE);

        //draw item quantity
        screen.drawString(mc.fontRenderer, "×" + this.itemStack.getCount(), this.x + ITEM_QUANTITY_REL_POS.x, this.y + ITEM_QUANTITY_REL_POS.y, 0xffffff);
    }

    @Override
    protected CoordinateUV getUV() {
        return UV;
    }

    public void setItemStack(ItemStack stack) {
        this.itemStack = stack;
    }
}
