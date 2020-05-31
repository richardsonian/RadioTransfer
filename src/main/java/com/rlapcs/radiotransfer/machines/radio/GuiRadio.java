package com.rlapcs.radiotransfer.machines.radio;

import com.enderio.core.common.util.NNList;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.GuiEmbedded3DBlockViewer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.TooltipContent;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiRadio extends AbstractGuiMachine {
    private GuiEmbedded3DBlockViewer multiblockViewer;

    private static final CoordinateXY VIEWER_POS = new CoordinateXY(7, 36);
    private static final DimensionWidthHeight VIEWER_SIZE = new DimensionWidthHeight(97, 92);
    private static final CoordinateXY MULTIBLOCK_NAME_POS = new CoordinateXY(9, 26);
    private static final CoordinateXY BLOCK_NAME_POS = new CoordinateXY(113, 26);
    private static final CoordinateXY INFO_START_POS = new CoordinateXY(113, 39);

    private boolean isClosed;
    private List<BlockPos> coords;
    private BlockPos selectedBlock;
    private boolean isHovering, wasHovering;

    public GuiRadio(TileRadio tileEntity, ContainerRadio container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(222, 146);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
        isHovering = false;
        wasHovering = false;
    }

    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        isHovering = false;
        wasHovering = false;

        coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();
        coords.add(tileEntity.getPos());
        if (selectedBlock == null)
            selectedBlock = coords.get(0);

        multiblockViewer = new GuiEmbedded3DBlockViewer(new NNList<>(coords), selectedBlock);
        isClosed = true;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
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

        wasHovering = isHovering;
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        CoordinateXY questionPos = pos.addTo(VIEWER_POS).addTo(VIEWER_SIZE).addTo(new CoordinateXY(-9, -9));
        isHovering = Mouse.getX() / scaleFactor >= questionPos.x - 2 && Mouse.getX() / scaleFactor <= questionPos.x + 8 && height - Mouse.getY() / scaleFactor >= questionPos.y - 2 && height - Mouse.getY() / scaleFactor <= questionPos.y + 9;
        if (isHovering)
            tooltip.activate(new TooltipContent(String.format("Select a block to see its status.\n%sRight-click%s to rotate.\n%sLeft-click%s to select a block.", TextFormatting.DARK_GRAY, TextFormatting.RESET, TextFormatting.DARK_GRAY, TextFormatting.RESET)));
        else if (wasHovering)
            tooltip.deactivate();

        super.drawScreen(mouseX, mouseY, partialTicks);

        drawString(mc.fontRenderer, TextFormatting.BOLD + "?", questionPos.x, questionPos.y, isHovering ? Color.GREEN.getRGB() : Color.WHITE.getRGB());

        drawString(mc.fontRenderer, "Multiblock viewer", pos.x + MULTIBLOCK_NAME_POS.x, pos.y + MULTIBLOCK_NAME_POS.y, Color.WHITE.getRGB());
        this.drawText();
        selectedBlock = multiblockViewer.draw(mouseX, mouseY, partialTicks, new CoordinateXY(VIEWER_POS.x + pos.x, this.height - (VIEWER_POS.y + VIEWER_SIZE.height + pos.y)), VIEWER_SIZE);
    }

    private void drawText() {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        CoordinateXY namePos = pos.addTo(BLOCK_NAME_POS);
        if (selectedBlock.equals(tileEntity.getPos())) {
            this.drawString(mc.fontRenderer, "Radio", namePos.x, namePos.y, Color.decode("#E8B07B").getRGB());
        } else {
            CoordinateXY infoPos = pos.addTo(INFO_START_POS);
            int liney = 0;
            MultiblockStatusData statusData = ((TileRadio) tileEntity).getMultiblockStatusData();
            MultiblockStatusData.NodeStatusEntry entry = statusData.getEntry(selectedBlock);
            List<MultiblockStatusData.Status> statuses = entry.getStatuses();
            this.drawString(mc.fontRenderer, entry.getBlock().getLocalizedName(), namePos.x, namePos.y, Color.decode("#E8B07B").getRGB());
            for (MultiblockStatusData.Status status : statuses) {
                String toDraw = "";
                switch (status.getKey()) {

                }
                this.drawString(mc.fontRenderer, status.toString(), infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                liney += mc.fontRenderer.FONT_HEIGHT + 2;
            }
        }
        RenderHelper.enableStandardItemLighting();
    }
}
