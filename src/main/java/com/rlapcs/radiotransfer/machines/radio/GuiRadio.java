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
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
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

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isClosed) {
            coords = ((TileRadio) tileEntity).getMultiblockStatusData().getAllNodePositions();
            coords.add(tileEntity.getPos());
            multiblockViewer.updateBlocksInList(new NNList<>(coords), selectedBlock);
            isClosed = false;
        }

        CoordinateXY questionPos = pos.addTo(VIEWER_POS).addTo(VIEWER_SIZE).addTo(new CoordinateXY(-9, -9));

        super.drawScreen(mouseX, mouseY, partialTicks);

        boolean isHoveringOnHelp = ((GuiHoverable) hoverables.get("Help")).check(new TooltipContent(String.format("Select a block to see its status.\n%sRight-click%s to rotate.\n%sLeft-click%s to select a block.", TextFormatting.DARK_GRAY, TextFormatting.RESET, TextFormatting.DARK_GRAY, TextFormatting.RESET)));
        drawString(mc.fontRenderer, TextFormatting.BOLD + "?", questionPos.x, questionPos.y, isHoveringOnHelp ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
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
                if (status instanceof StatusItemStack) {
                    StatusItemStack statusItemStack = (StatusItemStack) status;
                    String key = status.getKey() + ": ";
                    if (!statusItemStack.getValue().isEmpty()) {
                        this.drawString(mc.fontRenderer, key + "  ×" + ((StatusItemStack) status).getValue().getCount(), infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                        drawItem(statusItemStack.getValue(), new CoordinateXY(infoPos.x + GuiUtil.getLineLength(key) - 3, infoPos.y + liney - 1), status.getKey());
                    } else {
                        drawItem(new ItemStack(Items.ACACIA_BOAT), new CoordinateXY(infoPos.x + GuiUtil.getLineLength(key) - 3, infoPos.y + liney - 1), status.getKey());
                    }
                } else {
                    this.drawString(mc.fontRenderer, status.toString(), infoPos.x, infoPos.y + liney, Color.WHITE.getRGB());
                }
                liney += mc.fontRenderer.FONT_HEIGHT + 2;
            }
        }
        RenderHelper.enableStandardItemLighting();
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
