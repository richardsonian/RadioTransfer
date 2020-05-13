package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class GuiTooltip extends AbstractGuiWithVariableSize {
    private boolean isActive = false;
    private ITooltipContent content;

    public GuiTooltip(CoordinateXY pos, DimensionWidthHeight targetSize) {
        super(pos, targetSize);
    }

    public void activate(ITooltipContent content) {
        this.content = content;
        interpolatedSize = new DimensionWidthHeight(MINIMUM_SIZE, MINIMUM_SIZE);
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
        this.content = null;
    }

    @Override
    public void draw() {
        if (isActive) {
            targetSize = calculateTargetSize();
            pos = new CoordinateXY(Mouse.getX(), Mouse.getY());
            super.draw();
            if (isAtTargetSize)
                drawText();
        }
    }

    private void drawText() {
        drawString(mc.fontRenderer, content.getFormattedContent(), pos.x + 3, pos.y + 3, Color.white.getRGB());
    }

    private DimensionWidthHeight calculateTargetSize() {
        String[] lines = content.getFormattedContent().split("\n");
        int longestLine = 0;
        for (String line : lines)
            longestLine = Math.max(longestLine, getLineLength(line));
        return new DimensionWidthHeight(longestLine + 4, lines.length * 12 + 4);
    }

    private int getLineLength(String line) {
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
