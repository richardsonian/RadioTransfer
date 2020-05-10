package com.rlapcs.radiotransfer.machines.radio;

import com.enderio.core.common.util.NNList;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.GuiEmbedded3DBlockViewer;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.antennas.basic_antenna.BlockBasicAntenna;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.BlockItemEncoder;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

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
            coords = ((TileRadio) tileEntity).getMultiblockController().getMultiblockPositions();
            multiblockViewer.updateBlocksInList(new NNList<>(coords), selectedBlock);
            isClosed = false;
            sendDebugMessage("screen opened");
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        selectedBlock = multiblockViewer.draw(mouseX, mouseY, partialTicks, new CoordinateXY(VIEWER_POS.x + pos.x, this.height - (VIEWER_POS.y + VIEWER_SIZE.height + pos.y)), VIEWER_SIZE);
        drawText();
    }

    private void drawText() {

    }
}
