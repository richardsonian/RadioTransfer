package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.AbstractGuiWithVariableSize;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiTooltip extends AbstractGuiWithVariableSize {
    private boolean isActive = false;
    private ITooltipContent content;

    public GuiTooltip(CoordinateXY pos, DimensionWidthHeight targetSize) {
        super(pos, targetSize);
    }

    public void activate(ITooltipContent content) {
        //sendDebugMessage("isworking");
        this.content = content;
        isActive = true;
    }

    public void deactivate() {
        //sendDebugMessage("deactivate");
        isActive = false;
        this.content = null;
        interpolatedSize = new DimensionWidthHeight(MINIMUM_SIZE, MINIMUM_SIZE);
    }

    @Override
    public void draw() {
        if (isActive) {
            targetSize = calculateTargetSize();
            //sendDebugMessage("is active target size: " + targetSize);
            pos = new CoordinateXY(Mouse.getX(), Mouse.getY());
            super.draw();
            if (isAtTargetSize)
                drawText();
        }
    }

    private void drawText() {
        String[] lines = content.getFormattedContent().split("\n");
        for (int i = 0; i < lines.length; i++)
            drawString(mc.fontRenderer, lines[i], pos.x + 3, pos.y + i * 12 + 3, Color.white.getRGB());
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
