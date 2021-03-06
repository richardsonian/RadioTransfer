package com.rlapcs.radiotransfer.machines.radio;

import com.enderio.core.common.util.NNList;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiUtil;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.GuiEmbedded3DBlockViewer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiHoverable;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.TooltipContent;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData.StatusItemStack;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData.StatusList;
import com.rlapcs.radiotransfer.generic.multiblock.data.MultiblockStatusData.StatusUpgradeCard;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiRadio extends AbstractGuiMachine {
    private static final CoordinateXY VIEWER_POS = new CoordinateXY(7, 36);
    private static final DimensionWidthHeight VIEWER_SIZE = new DimensionWidthHeight(97, 92);
    private static final CoordinateXY MULTIBLOCK_NAME_POS = new CoordinateXY(9, 26);
    private static final CoordinateXY BLOCK_NAME_POS = new CoordinateXY(113, 26);
    private static final CoordinateXY INFO_START_POS = new CoordinateXY(113, 39);
    private static final double ITEM_SCALE = .625;

    private GuiEmbedded3DBlockViewer multiblockViewer;
    private boolean isClosed;
    private List<BlockPos> coords;
    private BlockPos selectedBlock;

    public GuiRadio(TileRadio tileEntity, ContainerRadio container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(222, 146);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
    }

    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));

        coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();
        coords.add(tileEntity.getPos());
        if (selectedBlock == null)
            selectedBlock = coords.get(0);

        multiblockViewer = new GuiEmbedded3DBlockViewer(new NNList<>(coords), selectedBlock);
        hoverables.put("Help", new GuiHoverable(pos.addTo(VIEWER_POS).addTo(VIEWER_SIZE).addTo(new CoordinateXY(-11, -11)), new DimensionWidthHeight(9, 9), this));
        isClosed = true;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
        isClosed = true;
    }

    public void updateMultiblockViewer() {
        coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();

        if (!coords.contains(selectedBlock))
            selectedBlock = coords.get(0);

        multiblockViewer.updateBlocksInList(new NNList<>(coords), selectedBlock);
    }

    @Override
    protected void preDrawScreen() {
        if (isClosed) {
            coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();
            multiblockViewer.updateBlocksInList(new NNList<>(coords), selectedBlock);
            isClosed = false;
        }
    }

    @Override
    protected void drawContentBeforeTooltip(int mouseX, int mouseY, float partialTicks) {
        CoordinateXY questionPos = pos.addTo(VIEWER_POS).addTo(VIEWER_SIZE).addTo(new CoordinateXY(-9, -9));
        boolean isHoveringOnHelp = ((GuiHoverable) hoverables.get("Help")).check(new TooltipContent(String.format("Select a block to see its status.\n%sRight-click%s to rotate.\n%sLeft-click%s to select a block.", TextFormatting.DARK_GRAY, TextFormatting.RESET, TextFormatting.DARK_GRAY, TextFormatting.RESET)));
        drawString(mc.fontRenderer, "Multiblock viewer", pos.x + MULTIBLOCK_NAME_POS.x, pos.y + MULTIBLOCK_NAME_POS.y, Color.WHITE.getRGB());
        this.drawText();
        selectedBlock = multiblockViewer.draw(mouseX, mouseY, partialTicks, new CoordinateXY(VIEWER_POS.x + pos.x, this.height - (VIEWER_POS.y + VIEWER_SIZE.height + pos.y)), VIEWER_SIZE);
        drawString(mc.fontRenderer, TextFormatting.BOLD + "?", questionPos.x, questionPos.y, isHoveringOnHelp ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
    }

    private void drawText() {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        CoordinateXY namePos = pos.addTo(BLOCK_NAME_POS);

        MultiblockStatusData statusData = ((TileRadio) tileEntity).getMultiblockStatusData();
        MultiblockStatusData.NodeStatusEntry entry = statusData.getEntry(selectedBlock);
        if (entry != null) {
            this.drawString(mc.fontRenderer, entry.getBlock().getLocalizedName(), namePos.x, namePos.y, Color.decode("#E8B07B").getRGB());
            List<MultiblockStatusData.Status> statuses = entry.getStatuses();
            iterateThroughStatuses(statuses, 0, 0);
        }

        RenderHelper.enableStandardItemLighting();
    }

    private int iterateThroughStatuses(List<MultiblockStatusData.Status> statuses, int offset, int liney) {
        CoordinateXY infoPos = pos.addTo(INFO_START_POS).addTo(new CoordinateXY(offset * 5, 0));
        for (MultiblockStatusData.Status status : statuses) {
            if (status instanceof StatusItemStack) {
                StatusItemStack statusItemStack = (StatusItemStack) status;
                String key = status.getKey() + ": ";
                if (!statusItemStack.getValue().isEmpty()) {
                    this.drawString(mc.fontRenderer, key + "  ×" + ((StatusItemStack) status).getValue().getCount(), infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                    drawItem(statusItemStack.getValue(), new CoordinateXY(infoPos.x + GuiUtil.getLineLength(key) - 3, infoPos.y + liney - 1), status.getKey());
                } else {
                    this.drawString(mc.fontRenderer, status.toString(), infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                }
            } else if (status instanceof StatusList) {
                StatusList statusList = (StatusList) status;
                this.drawString(mc.fontRenderer, status.getKey() + ":", infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                liney = iterateThroughStatuses(statusList.getValue(), offset + 1, liney + mc.fontRenderer.FONT_HEIGHT + 2) - mc.fontRenderer.FONT_HEIGHT - 2;
            } else if (status instanceof StatusUpgradeCard) {
                StatusUpgradeCard statusUpgradeCard = (StatusUpgradeCard) status;
                drawItem(statusUpgradeCard.getItemStack(), new CoordinateXY(infoPos.x, infoPos.y + liney - 1), String.valueOf(statusUpgradeCard.hashCode()));
                this.drawString(mc.fontRenderer, "×" + statusUpgradeCard.getQuantity(), infoPos.x + 12, infoPos.y + liney, Color.WHITE.getRGB());
            } else {
                ArrayList<String> lines = new ArrayList<>();
                lines.add(status.toString());
                GuiUtil.formatLines(lines, 100, true);
                for (String line : lines) {
                    this.drawString(mc.fontRenderer, line, infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                    liney += mc.fontRenderer.FONT_HEIGHT + 2;
                }
                liney -= mc.fontRenderer.FONT_HEIGHT + 2;
            }
            liney += mc.fontRenderer.FONT_HEIGHT + 2;
        }
        return liney;
    }

    private void drawItem(ItemStack itemStack, CoordinateXY pos, String label) {
        if (hoverables.get(label) == null)
            hoverables.put(label, new GuiHoverable(pos, new DimensionWidthHeight((int) (16 * ITEM_SCALE), (int) (16 * ITEM_SCALE)), this));
        ((GuiHoverable) hoverables.get(label)).check(new TooltipContent(itemStack.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL)));

        GL11.glScaled(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) (pos.x / ITEM_SCALE), (int) (pos.y / ITEM_SCALE));
        GL11.glScaled(1 / ITEM_SCALE, 1 / ITEM_SCALE, 1 / ITEM_SCALE);
    }
}
