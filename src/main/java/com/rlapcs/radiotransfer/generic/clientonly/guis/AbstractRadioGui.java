package com.rlapcs.radiotransfer.generic.clientonly.guis;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;


public abstract class AbstractRadioGui extends AbstractMachineGui {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation texture = new ResourceLocation(RadioTransfer.MODID, "textures" +
            "/gui/radio.png");

    public AbstractRadioGui(TileEntity tileEntity, Container container) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);

    }

    @Override
    public void initGui() {
        super.initGui();
        //add buttons here
        //buttonList.add();

    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); //draws background

        //draw buttons:
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //draw text
        String temp = "136.2 mHz";
        fontRenderer.drawString(temp,  14,  27, Color.white.getRGB());
    }

}
