package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.guis.clientonly.buttons.GuiIncrementButton.IncrementType;
import com.rlapcs.radiotransfer.generic.guis.clientonly.buttons.GuiToggleSliderButton;
import com.rlapcs.radiotransfer.generic.network.messages.MessageActivateTileRadio;
import com.rlapcs.radiotransfer.generic.network.messages.MessageUpdateTileRadioFrequency;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileRadio;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class AbstractRadioGui extends AbstractMachineGui {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    protected static int FREQUENCY_INCREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_INCREMENT_X; //location should be overridden
    protected static int FREQUENCY_INCREMENT_Y;

    protected static int FREQUENCY_DECREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_DECREMENT_X; //location should be overridden
    protected static int FREQUENCY_DECREMENT_Y;

    protected static int ACTIVATE_ID = getNextButtonID();
    protected static int ACTIVATE_ON_X = 78;
    protected static int ACTIVATE_ON_Y = 15;
    protected static int ACTIVATE_OFF_X = 78;
    protected static int ACTIVATE_OFF_Y = 36;

    public AbstractRadioGui(TileEntity tileEntity, Container container, ResourceLocation background) {
        super(tileEntity, container, WIDTH, HEIGHT, background);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == FREQUENCY_INCREMENT_ID) {
            sendChatMessage("frequency incremented"); //Debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileRadioFrequency(tileEntity, true));
            ((AbstractTileRadio) tileEntity).changeFrequency(true);
        }
        if(button.id == FREQUENCY_DECREMENT_ID) {
            sendChatMessage("frequency decremented"); //debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileRadioFrequency(tileEntity, false));
            ((AbstractTileRadio) tileEntity).changeFrequency(false);
        }
        if(button.id == ACTIVATE_ID) {
            sendChatMessage("activate button pressed"); //DEBUG

            GuiToggleSliderButton activateButton = (GuiToggleSliderButton) button;
            int pos = activateButton.flipState();

            //update server tileEntity
            ModNetworkMessages.INSTANCE.sendToServer(new MessageActivateTileRadio(tileEntity, pos == 1));
            //update client tileEntity
            ((AbstractTileRadio) tileEntity).setActivated(pos == 1);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        //frequency increment button
        this.addButton(new GuiIncrementButton(FREQUENCY_INCREMENT_ID, guiLeft + FREQUENCY_INCREMENT_X, guiTop + FREQUENCY_INCREMENT_Y,
                GuiIncrementButton.IncrementType.UP));

        //frequency decrement button
        this.addButton(new GuiIncrementButton(FREQUENCY_DECREMENT_ID, guiLeft + FREQUENCY_DECREMENT_X, guiTop + FREQUENCY_DECREMENT_Y,
                GuiIncrementButton.IncrementType.DOWN));

        //activate button
        this.addButton(new GuiToggleSliderButton(ACTIVATE_ID, ((AbstractTileRadio) tileEntity).getActivated() ? 1 : 2, guiLeft + ACTIVATE_ON_X, guiTop + ACTIVATE_ON_Y,
                guiLeft + ACTIVATE_OFF_X, guiTop + ACTIVATE_OFF_Y));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); //draws background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //draw text
        String name = Minecraft.getMinecraft().world.getBlockState(tileEntity.getPos()).getBlock().getLocalizedName();
        fontRenderer.drawString(name, 5, 5, Color.white.getRGB());

        fontRenderer.drawString("on", 64, 18, Color.white.getRGB());
        fontRenderer.drawString("off", 60, 44, Color.white.getRGB());

        String frequency = "" + ((AbstractTileRadio) tileEntity).getFrequency();
        fontRenderer.drawString(frequency,  FREQUENCY_INCREMENT_X + 6,  FREQUENCY_DECREMENT_Y - 15, Color.white.getRGB());
    }

}
