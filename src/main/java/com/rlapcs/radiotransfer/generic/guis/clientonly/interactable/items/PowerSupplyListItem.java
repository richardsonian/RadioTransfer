package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockPowerUsageData;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import scala.collection.immutable.List;

public class PowerSupplyListItem extends AbstractGuiListItem {
    /* copied values from AbstractItemProcessorGuiListItem; should change */
    public static final CoordinateUV UV = new CoordinateUV(0,0); //has getter method to be accessed by superclass if needed
    public static final CoordinateXY DIMS = new CoordinateXY(66, 15);
    public static final CoordinateXY ITEM_REL_POS = new CoordinateXY(3, 2);
    public static final double ITEM_SCALE = .6875;
    public static final CoordinateXY POWER_TEXT_REL_POS = new CoordinateXY(15, 4);

    //instance variables
    private ItemStack renderItem; //temp

    public PowerSupplyListItem(int id, int index, CoordinateXY pos, TilePowerSupply tile) {
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
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        renderer.renderItemIntoGUI(this.renderItem, (int)((this.x + ITEM_REL_POS.x) / ITEM_SCALE), (int)((this.y + ITEM_REL_POS.y) / ITEM_SCALE));
        GL11.glScaled(1 / ITEM_SCALE,1 / ITEM_SCALE,1 / ITEM_SCALE);

        //draw power usage
        screen.drawString(mc.fontRenderer, "500 FE", this.x + POWER_TEXT_REL_POS.x, this.y + POWER_TEXT_REL_POS.y, 0xffffff);
        RenderHelper.enableStandardItemLighting();

        wasClicking = flag;
    }

    @Override
    protected CoordinateUV getUV() {
        return UV;
    }
}
