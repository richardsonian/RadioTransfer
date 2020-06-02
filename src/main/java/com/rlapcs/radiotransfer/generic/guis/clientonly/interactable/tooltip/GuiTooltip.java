package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiUtil;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.AbstractGuiWithVariableSize;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rlapcs.radiotransfer.generic.guis.clientonly.GuiUtil.getLineLength;
import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiTooltip extends AbstractGuiWithVariableSize {
    private boolean isActive = false;
    protected ITooltipContent content;
    private List<String> lines;

    public GuiTooltip(CoordinateXY pos, DimensionWidthHeight targetSize) {
        super(pos, targetSize);
    }

    public void activate(ITooltipContent content) {
        //sendDebugMessage(this.toString() + " isworking");
        this.content = content;
        isActive = true;
    }

    public void deactivate() {
        //sendDebugMessage(this.toString() + " deactivate");
        isActive = false;
        this.content = null;
        interpolatedSize = new DimensionWidthHeight(MINIMUM_SIZE, MINIMUM_SIZE);
    }

    public void draw() {
        if (isActive) {
            int scaleFactor = new ScaledResolution(mc).getScaleFactor();
            pos = new CoordinateXY(Mouse.getX() / scaleFactor, this.height - Mouse.getY() / scaleFactor);
            int maxAllowableSize = pos.x + interpolatedSize.width > this.width ? pos.x : this.width - pos.x;
            lines = new ArrayList<>();
            lines.addAll(content.getFormattedContent());
            GuiUtil.formatLines(lines, maxAllowableSize, pos.x + interpolatedSize.width > this.width);
            targetSize = calculateTargetSize();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, 500);
            super.draw();
            //sendDebugMessage(this.toString() + " is active target size: " + targetSize + "  real size: " + interpolatedSize);
            if (isAtTargetSize) {
                RenderHelper.disableStandardItemLighting();
                RenderHelper.enableGUIStandardItemLighting();
                renderContent();
                RenderHelper.enableStandardItemLighting();
            }
            GlStateManager.popMatrix();
        }
    }

    protected void renderContent() {
        for (int i = 0; i < lines.size(); i++)
            drawString(mc.fontRenderer, lines.get(i), pos.x + 3, pos.y + i * 12 + 3, Color.white.getRGB());
    }

    protected DimensionWidthHeight calculateTargetSize() {
        int longestLine = 0;
        for (String line : lines)
            longestLine = Math.max(longestLine, getLineLength(line));
        return new DimensionWidthHeight(longestLine + 4, lines.size() * 12);
    }
}
