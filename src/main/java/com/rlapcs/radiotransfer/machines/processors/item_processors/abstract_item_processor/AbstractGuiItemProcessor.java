package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMachine<T> {
    private static final int WIDTH = 188;
    private static final int HEIGHT = 197;

    public static final int PROGRESS_BAR_U = 8;
    public static final int PROGRESS_BAR_V = 15;
    public static final int PROGRESS_BAR_WIDTH = 9;
    public static final int PROGRESS_BAR_HEIGHT = 6;

    public AbstractGuiItemProcessor(T tileEntity, AbstractContainerItemProcessor container, ResourceLocation texture) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        sendChatMessage("Player now tracking" + tileEntity);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
        sendChatMessage("Player no longer tracking " + tileEntity);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        //progress bar
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png"));
        double progress = tileEntity.getFractionOfProcessCompleted();
        //sendChatMessage("process time completed: " + progress);
        drawTexturedModalRect(guiLeft + getProgressBarCoords()[0], guiTop + getProgressBarCoords()[1], PROGRESS_BAR_U, PROGRESS_BAR_V,
                ((int)(progress * PROGRESS_BAR_WIDTH)), PROGRESS_BAR_HEIGHT);
    }

    protected abstract int[] getProgressBarCoords();
}
