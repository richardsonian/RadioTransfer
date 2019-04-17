package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.RadioTransfer;

import com.rlapcs.radiotransfer.generic.clientonly.guis.GuiSelectButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiTransmitter extends GuiContainer {

    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures" +
            "/gui/demogui-test.png");

    public GuiTransmitter(TileTransmitter tileEntity, ContainerTransmitter container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();
        //add buttons here
        //buttonList.add();

    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}