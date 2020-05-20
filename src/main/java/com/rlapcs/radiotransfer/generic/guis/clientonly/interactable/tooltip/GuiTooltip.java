package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.AbstractGuiWithVariableSize;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.power_supply.GuiPowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiTooltip extends AbstractGuiWithVariableSize {
    private boolean isActive = false;
    protected ITooltipContent content;

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
            targetSize = calculateTargetSize();
            int scaleFactor = new ScaledResolution(mc).getScaleFactor();
            pos = new CoordinateXY(Mouse.getX() / scaleFactor, this.height - Mouse.getY() / scaleFactor);
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
        String[] lines = content.getFormattedContent().split("\n");
        for (int i = 0; i < lines.length; i++)
            drawString(mc.fontRenderer, lines[i], pos.x + 3, pos.y + i * 12 + 3, Color.white.getRGB());
    }

    protected DimensionWidthHeight calculateTargetSize() {
        String[] lines = content.getFormattedContent().split("\n");
        int longestLine = 0;
        for (String line : lines)
            longestLine = Math.max(longestLine, getLineLength(line));
        return new DimensionWidthHeight(longestLine + 4, lines.length * 12);
    }

    protected int getLineLength(String line) {
        char[] letters = line.toCharArray();
        int length = 1;
        for (char letter : letters) {
            switch (letter) {
                case 't':
                case ' ':
                    length += 3; break;
                case 'k':
                case 'f':
                    length += 4; break;
                case 'i':
                case '.':
                case ':':
                    length += 1; break;
                case 'l':
                    length += 2; break;
                default:
                    length += 5;
            }
            length += 1;
        }
        return length;
    }
}
