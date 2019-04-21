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

    public static final String TEXTURE_PATH = "textures/gui/radio.png";

    public static final int INCREMENT_ID = 1;
    public static final int INCREMENT_X = 20;
    public static final int INCREMENT_Y = 10;

    public static final int DECREMENT_ID = 2;
    public static final int DECREMENT_X = 20;
    public static final int DECREMENT_Y = 45;

    public static final int ACTIVATE_ID = 3;
    public static final int ACTIVATE_ON_Y = 15;
    public static final int ACTIVATE_OFF_X = 75;
    public static final int ACTIVATE_OFF_Y = 35;
    public static final int ACTIVATE_ON_X = 75;

    private static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, TEXTURE_PATH);

    public AbstractRadioGui(TileEntity tileEntity, Container container) {
        super(tileEntity, container, WIDTH, HEIGHT, background);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == INCREMENT_ID) {
            sendChatMessage("frequency incremented"); //Debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileRadioFrequency(tileEntity, true));
            ((AbstractTileRadio) tileEntity).changeFrequency(true);
        }
        if(button.id == DECREMENT_ID) {
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

        //increment button
        addButton(new GuiIncrementButton(INCREMENT_ID, guiLeft + INCREMENT_X, guiTop + INCREMENT_Y,
                IncrementType.UP));

        //decrement button
        addButton(new GuiIncrementButton(DECREMENT_ID, guiLeft + DECREMENT_X, guiTop + DECREMENT_Y,
                IncrementType.DOWN));

        //activate button
        addButton(new GuiToggleSliderButton(ACTIVATE_ID, ((AbstractTileRadio) tileEntity).getActivated() ? 1 : 2, guiLeft + ACTIVATE_ON_X, guiTop + ACTIVATE_ON_Y,
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

        String frequency = "" + ((AbstractTileRadio) tileEntity).getFrequency();
        fontRenderer.drawString(frequency,  18,  30, Color.white.getRGB());
    }

}
