package com.rlapcs.radiotransfer.client.guis;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.common.containers.DemoContainer;
import com.rlapcs.radiotransfer.common.tileEntities.DemoBlockTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class DemoBlockGui extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures" +
            "/gui/demogui-test.png");

    public DemoBlockGui(DemoBlockTileEntity tileEntity, DemoContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}