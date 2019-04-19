package com.rlapcs.radiotransfer.generic.clientonly.guis;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.clientonly.guis.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.clientonly.guis.buttons.GuiToggleSliderButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class AbstractRadioGui extends AbstractMachineGui {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    public static final String TEXTURE_PATH = "textures/gui/radio.png";

    public static final int INCREMENT_ID = 1;
    public static final String INCREMENT_TEXTURE_PATH = "textures/gui/button-top-static.png";
    public static final int INCREMENT_X = 20;
    public static final int INCREMENT_Y = 20;
    public static final int INCREMENT_WIDTH = 20;
    public static final int INCREMENT_HEIGHT = 20;

    public static final int DECREMENT_ID = 2;
    public static final String DECREMENT_TEXTURE_PATH = "textures/gui/button-bottom-static.png";
    public static final int DECREMENT_X = 20;
    public static final int DECREMENT_Y = 50;
    public static final int DECREMENT_WIDTH = 20;
    public static final int DECREMENT_HEIGHT = 20;

    public static final int ACTIVATE_ID = 3;
    public static final String ACTIVATE_TEXTURE_PATH = "textures/gui/slider.png";
    public static final int ACTIVATE_X = 75;
    public static final int ACTIVATE_Y = 20;
    public static final int ACTIVATE_WIDTH = 20;
    public static final int ACTIVATE_HEIGHT = 20;

    private GuiIncrementButton frequencyIncrementButton;
    private GuiIncrementButton frequencyDecrementButton;
    private GuiToggleSliderButton activateButton;

    private static final ResourceLocation texture = new ResourceLocation(RadioTransfer.MODID, TEXTURE_PATH);

    public AbstractRadioGui(TileEntity tileEntity, Container container) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == INCREMENT_ID) {sendChatMessage("frequency incremented");}
        if(button.id == DECREMENT_ID) {sendChatMessage("frequency decremented");}
        if(button.id == ACTIVATE_ID) {sendChatMessage("activate button pressed");}
    }

    @Override
    public void initGui() {
        super.initGui();
        //add buttons here

        //increment button
        addButton(new GuiIncrementButton(INCREMENT_ID, guiLeft + INCREMENT_X, guiTop + INCREMENT_Y,
                INCREMENT_WIDTH, INCREMENT_HEIGHT, new ResourceLocation(RadioTransfer.MODID, INCREMENT_TEXTURE_PATH)));

        //decrement button
        addButton(new GuiIncrementButton(DECREMENT_ID, guiLeft + DECREMENT_X, guiTop + DECREMENT_Y,
                DECREMENT_WIDTH, DECREMENT_HEIGHT, new ResourceLocation(RadioTransfer.MODID, DECREMENT_TEXTURE_PATH)));

        //activate button
        addButton(new GuiToggleSliderButton(ACTIVATE_ID, guiLeft + ACTIVATE_X, guiTop + ACTIVATE_Y,
                ACTIVATE_WIDTH, ACTIVATE_HEIGHT, new ResourceLocation(RadioTransfer.MODID, ACTIVATE_TEXTURE_PATH)));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); //draws background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //draw text
        String temp = "136.2";
        fontRenderer.drawString(temp,  15,  28, Color.white.getRGB());
    }

}
