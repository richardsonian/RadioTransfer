package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable;

import com.enderio.core.common.util.NNList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.IGui;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.renderers.RendererMultiblock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class GuiEmbedded3DBlockViewer implements IGui {
    private RendererMultiblock viewer;
    private CoordinateXY pos;
    private DimensionWidthHeight size;

    public GuiEmbedded3DBlockViewer(NNList<BlockPos> posCoords, BlockPos selected) {
        viewer = new RendererMultiblock(posCoords, selected);
        viewer.init();
    }

    public BlockPos draw(int mouseX, int mouseY, float partialTicks, CoordinateXY pos, DimensionWidthHeight size) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.pos = pos.scale(scaledResolution.getScaleFactor());
        this.size = size.scale(scaledResolution.getScaleFactor());
        return viewer.drawScreen(mouseX, mouseY, partialTicks, new Rectangle(this.pos.x, this.pos.y, this.size.width, this.size.height));
    }

    public void updateBlocksInList(NNList<BlockPos> posCoords, BlockPos selected) {
        viewer = new RendererMultiblock(posCoords, selected);
        viewer.init();
    }

    @Override
    public CoordinateXY getGuiPos() {
        return pos;
    }

    @Override
    public DimensionWidthHeight getGuiSize() {
        return size;
    }
}
