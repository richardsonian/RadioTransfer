package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public abstract class AbstractGuiWithVariableSize extends GuiScreen {
    private static final CoordinateUV[] CORNERS = {
            new CoordinateUV(0, 18), // top left
            new CoordinateUV(186, 18), // top right
            new CoordinateUV(0, 195), // bottom left
            new CoordinateUV(186, 195)}; // bottom right
    private static final Color OUTLINE = Color.decode("#939393");
    private static final Color INTERIOR = Color.decode("#383838");
    protected static final int MINIMUM_SIZE = 4;

    protected CoordinateXY pos;
    protected DimensionWidthHeight targetSize;
    protected DimensionWidthHeight interpolatedSize;
    protected boolean isAtTargetSize;

    public AbstractGuiWithVariableSize(CoordinateXY pos, DimensionWidthHeight targetSize) {
        this.pos = pos;
        this.targetSize = targetSize;
        this.interpolatedSize = new DimensionWidthHeight(MINIMUM_SIZE, MINIMUM_SIZE);
        this.isAtTargetSize = false;
    }

    public void draw() {
        // CRUNCH POS & SIZE NUMBERS
        int widthDifference = targetSize.width - interpolatedSize.width;
        int heightDifference = targetSize.height - interpolatedSize.height;
        int widthToAdd = Math.abs(widthDifference) < 4 ? MathHelper.clamp(Integer.compare(widthDifference, 0), -1, 1) : (widthDifference) / 4;
        int heightToAdd = Math.abs(heightDifference) < 4 ? MathHelper.clamp(Integer.compare(heightDifference, 0), -1, 1) : (heightDifference) / 4;
        interpolatedSize = interpolatedSize.addTo(new DimensionWidthHeight(widthToAdd, heightToAdd));
        isAtTargetSize = interpolatedSize.equals(targetSize);

        if (pos.x + interpolatedSize.width > this.width)
            pos = pos.addTo(new CoordinateXY(-interpolatedSize.width, 0));

        // SETUP
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_decoder.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // DRAW BODY AND OUTLINE
        drawRect(pos.x + 1, pos.y + 1, pos.x + interpolatedSize.width, pos.y + interpolatedSize.height, INTERIOR.getRGB());

        drawVerticalLine(pos.x, pos.y + 1, pos.y + interpolatedSize.height - 1, OUTLINE.getRGB());
        drawVerticalLine(pos.x + interpolatedSize.width, pos.y + 1, pos.y + interpolatedSize.height - 1, OUTLINE.getRGB());
        drawHorizontalLine(pos.x + 2, pos.x + interpolatedSize.width - 2, pos.y, OUTLINE.getRGB());
        drawHorizontalLine(pos.x + 2, pos.x + interpolatedSize.width - 2, pos.y + interpolatedSize.height, OUTLINE.getRGB());

        // DRAW CORNERS
        drawTexturedModalRect(pos.x, pos.y, CORNERS[0].u, CORNERS[0].v, 2, 2);
        drawTexturedModalRect(pos.x + interpolatedSize.width - 1, pos.y, CORNERS[1].u, CORNERS[1].v, 2, 2);
        drawTexturedModalRect(pos.x, pos.y + interpolatedSize.height - 1, CORNERS[2].u, CORNERS[2].v, 2, 2);
        drawTexturedModalRect(pos.x + interpolatedSize.width - 1, pos.y + interpolatedSize.height - 1, CORNERS[3].u, CORNERS[3].v, 2, 2);

        RenderHelper.enableStandardItemLighting();
    }
}
