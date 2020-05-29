package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockPowerUsageData.PowerUsageEntry;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockPowerUsageData.PowerUsageEntry.UpgradeCardPowerEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class PowerSupplyEntryTooltip extends GuiTooltip {
    private static final double ITEM_SCALE = .6875;
    private int yOffset;
    private ArrayList<String> textBeingDrawn;
    private boolean isFirstPass;

    public PowerSupplyEntryTooltip(CoordinateXY pos, DimensionWidthHeight targetSize) {
        super(pos, targetSize);
        textBeingDrawn = new ArrayList<>();
        yOffset = 15;
        isFirstPass = false;
    }

    @Override
    protected void renderContent() {
        PowerUsageEntry entry = (PowerUsageEntry) this.content;
        yOffset = 0;
        textBeingDrawn.clear();

        drawText(TextFormatting.UNDERLINE + entry.getTitle() + TextFormatting.RESET, Color.decode("#E8B07B"));
        if (!isFirstPass)
            drawString(mc.fontRenderer, String.format("%s%sACTIVE", TextFormatting.BOLD, entry.isActive ? "" : "IN"), getActiveX(), pos.y + 3, entry.isActive ? Color.GREEN.getRGB() : Color.RED.getRGB());
        if (entry.requiresConstantPower()) {
            drawText("Base: " + entry.basePowerPerTick + " FE/t", Color.WHITE);
            if (entry.getSortedUpgradeCardConstantCosts().size() > 0) {
                for (UpgradeCardPowerEntry cardEntry : entry.getSortedUpgradeCardConstantCosts()) {
                    renderCardItem(new ItemStack(cardEntry.item));
                    drawText(" -   " + cardEntry + " FE/t", Color.WHITE);
                }
            }
            drawText(String.format("Total per tick: %d FE/t", entry.effectivePowerPerTick), Color.WHITE);
        } else
            drawText("Does not require constant power.", Color.GRAY);

        yOffset++;
        if (!isFirstPass)
            drawHorizontalLine(pos.x + 4, pos.x + interpolatedSize.width - 4, pos.y + yOffset, Color.decode("#202020").getRGB());
        yOffset++;

        if (entry.requiresProcessPower()) {
            drawText("Base: " + entry.basePowerPerProcess + " FE/process", Color.WHITE);
            if (entry.getSortedUpgradeCardProcessCosts().size() > 0) {
                for (UpgradeCardPowerEntry cardEntry : entry.getSortedUpgradeCardProcessCosts()) {
                    renderCardItem(new ItemStack(cardEntry.item));
                    drawText(" -   " + cardEntry + " FE/process", Color.WHITE);
                }
            }
            drawText(String.format("Total per tick: %d FE/process", entry.effectivePowerPerProcess), Color.WHITE);
        } else
            drawText("Does not require power per process.", Color.GRAY);
    }

    @Override
    protected DimensionWidthHeight calculateTargetSize() {
        isFirstPass = true;
        renderContent();
        isFirstPass = false;
        int width = 15;
        if (textBeingDrawn.size() > 0) {
            width = getLineLength(textBeingDrawn.get(0));
            for (String text : textBeingDrawn)
                width = Math.max(width, getLineLength(text));
        }
        return new DimensionWidthHeight(width + 4, yOffset + 1);
    }

    private void drawText(String text, Color color) {
        if (!isFirstPass)
            drawString(mc.fontRenderer, text, pos.x + 3, pos.y + yOffset + 3, color.getRGB());
        yOffset += 12;
        textBeingDrawn.add(text);
    }

    private void renderCardItem(ItemStack item) {
        GL11.glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        if (!isFirstPass)
            mc.getRenderItem().renderItemIntoGUI(item, (int)((pos.x + 13) / ITEM_SCALE), (int)((pos.y + yOffset + 1) / ITEM_SCALE));
        GL11.glScaled(1 / ITEM_SCALE,1 / ITEM_SCALE,1 / ITEM_SCALE);
    }

    private int getActiveX() {
        return pos.x + targetSize.width - getLineLength(String.format("%s", ((PowerUsageEntry) this.content).isActive ? "ACTIVE" : "INACTIVE")) - 5;
    }
}
