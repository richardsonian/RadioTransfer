package com.rlapcs.radiotransfer.machines.radio;

import com.enderio.core.common.util.NNList;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.GuiEmbedded3DBlockViewer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.indicators.GuiPowerIndicator;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.List;

public class GuiRadio extends AbstractGuiMachine {
    private GuiEmbedded3DBlockViewer multiblockViewer;

    private static final CoordinateXY VIEWER_POS = new CoordinateXY(8, 25);
    private static final DimensionWidthHeight VIEWER_SIZE = new DimensionWidthHeight(95, 73);

    private boolean isClosed;
    private List<BlockPos> coords;
    private BlockPos selectedBlock;

    public GuiRadio(TileRadio tileEntity, ContainerRadio container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 163);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
    }

    public void initGui() {
        super.initGui();
        coords = ((TileRadio) tileEntity).getMultiblockController().getMultiblockPositions();
        selectedBlock = coords.get(0);
        multiblockViewer = new GuiEmbedded3DBlockViewer(new NNList<>(coords), selectedBlock);
        isClosed = true;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        isClosed = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isClosed) {
            coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();
            coords.add(tileEntity.getPos());
            multiblockViewer.updateBlocksInList(new NNList<>(coords), selectedBlock);
            isClosed = false;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        selectedBlock = multiblockViewer.draw(mouseX, mouseY, partialTicks, new CoordinateXY(VIEWER_POS.x + pos.x, this.height - (VIEWER_POS.y + VIEWER_SIZE.height + pos.y)), VIEWER_SIZE);
        drawText();
    }

    private void drawText() {
        CoordinateXY startPos = VIEWER_POS.addTo(VIEWER_SIZE).addTo(new CoordinateXY(10, 0));
        int liney = 0;

        if(selectedBlock.equals(tileEntity.getPos())) {
            drawString(mc.fontRenderer, "Radio Selected.", startPos.x, startPos.y + liney, Color.white.getRGB());
        }
        else {
            MultiblockStatusData statusData = ((TileRadio)tileEntity).getMultiblockStatusData();
            MultiblockStatusData.NodeStatusEntry entry = statusData.getEntry(selectedBlock);
            List<MultiblockStatusData.Status> statuses = entry.getStatuses();
            for (MultiblockStatusData.Status status : statuses) {
                drawString(mc.fontRenderer, status.toString(), startPos.x, startPos.y + liney, Color.white.getRGB());
                liney += mc.fontRenderer.FONT_HEIGHT;
            }
        }
    }
}
