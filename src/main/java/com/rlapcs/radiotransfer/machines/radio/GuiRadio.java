package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.BlockItemEncoder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiRadio extends GuiScreen {
    private ResourceLocation texture;
    private TileRadio tileEntity;
    private int xSize = 188;
    private int ySize = 163;
    protected int guiLeft;
    protected int guiTop;

    public GuiRadio(TileRadio tileEntity) {
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
        this.tileEntity = tileEntity;
        //sendDebugMessage("construct");
    }

    public void initGui() {
        super.initGui();
        //sendDebugMessage("initgui");
        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //sendDebugMessage("drawscreen");
        drawRect(0, 0, width, height, 0x70000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawText();
        renderMultiblockLayout();
    }

    private void renderMultiblockLayout() {
        itemRender.renderItem(new ItemStack(new BlockItemEncoder(), 1), ItemCameraTransforms.TransformType.GUI);
    }

    private void drawText() {

    }
}
