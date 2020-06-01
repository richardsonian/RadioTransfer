package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.AbstractGuiWithVariableSize;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.*;
import java.util.List;

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
            formatLines();
            targetSize = calculateTargetSize();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, 1000);
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
        return new DimensionWidthHeight(longestLine, lines.size() * 12);
    }

    private void formatLines() {
        int maxAllowableSize = pos.x + interpolatedSize.width > this.width ? pos.x : this.width - pos.x;
        lines = new ArrayList<>();
        lines.addAll(Arrays.asList(content.getFormattedContent().split("\n")));
        if (pos.x + interpolatedSize.width > this.width) {
            List<String> linesToIterate = new ArrayList<>();
            linesToIterate.addAll(lines);
            int index = 0;
            for (String line : linesToIterate) {
                //sendDebugMessage("og line: " + line + " : index " + index);
                //sendDebugMessage("oopsies: " + getLineLength(line) + " : " + (maxAllowableSize - 4));
                if (getLineLength(line) > maxAllowableSize - 8) {
                    ArrayList<String> pieces = new ArrayList<>();
                    while (getLineLength(line) > maxAllowableSize - 6) {
                        //sendDebugMessage("Lineeeee " + line);
                        String chunk = getMaxWithinSize(line, maxAllowableSize - 6);
                        //sendDebugMessage("chonk " + chunk);
                        pieces.add(chunk);
                        line = line.substring(chunk.length());
                        //sendDebugMessage("newlineeee " + line);
                    }
                    pieces.add(line);
                    lines.remove(index);
                    Collections.reverse(pieces);
                    for (String piece : pieces)
                        lines.add(index, piece);
                    index += pieces.size() - 1;
                }
                index++;
            }
        }
    }

    private String getMaxWithinSize(String text, int maxSize) {
        int maxIndex = 0;
        while (getLineLength(text.substring(0, maxIndex)) < maxSize)
            maxIndex++;
        return text.substring(0, maxIndex);
    }
}
