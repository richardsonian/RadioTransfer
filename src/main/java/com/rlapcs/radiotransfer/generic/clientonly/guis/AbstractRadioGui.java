package com.rlapcs.radiotransfer.generic.clientonly.guis;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.clientonly.guis.buttons.GuiIncrementButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;



public abstract class AbstractRadioGui extends AbstractMachineGui {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    public static final String TEXTURE_PATH = "textures/gui/radio.png";
    public static final String INCREMENT_TEXTURE_PATH = "textures/gui/buttonTopStatic.png";
    public static final String DECREMENT_TEXTURE_PATH = "textures/gui/buttonBottomStatic.png";
    public static final String ACTIVATE_TEXTURE_PATH = "textures/gui/buttonBottomStatic.png";

    private GuiIncrementButton frequencyIncrementButton = new GuiIncrementButton(0, 10, 10,
            new ResourceLocation(RadioTransfer.MODID, INCREMENT_TEXTURE_PATH));
    private GuiIncrementButton frequencyDecrementButton = new GuiIncrementButton(1, 30, 30,
            new ResourceLocation(RadioTransfer.MODID, DECREMENT_TEXTURE_PATH));
    private GuiIncrementButton activateButton = new GuiIncrementButton(2, 50, 50,
            new ResourceLocation(RadioTransfer.MODID, ACTIVATE_TEXTURE_PATH));

    private static final ResourceLocation texture = new ResourceLocation(RadioTransfer.MODID, TEXTURE_PATH);

    public AbstractRadioGui(TileEntity tileEntity, Container container) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == frequencyIncrementButton) {sendChatMessage("frequency incremented");}
        if(button == frequencyDecrementButton) {sendChatMessage("frequency decremented");}
        if(button == activateButton) {sendChatMessage("activate button pressed");}
    }

    @Override
    public void initGui() {
        super.initGui();
        //add buttons here
        addButton(frequencyIncrementButton);
        addButton(frequencyDecrementButton);
        addButton(activateButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); //draws background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //draw text
    }

}
