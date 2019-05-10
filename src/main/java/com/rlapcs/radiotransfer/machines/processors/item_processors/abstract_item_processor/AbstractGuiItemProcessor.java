package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMachine<T> {
    private static final int WIDTH = 188;
    private static final int HEIGHT = 197;

    private static final int[] LIST_POS = {115, 28};

    public static final int PROGRESS_BAR_U = 8;
    public static final int PROGRESS_BAR_V = 15;
    public static final int PROGRESS_BAR_WIDTH = 9;
    public static final int PROGRESS_BAR_HEIGHT = 6;

    private GuiList visual;
    private ItemPacketQueue queue;
    private boolean wasClicking;
    private boolean isScrolling;
    private GuiDraggableSliderButton bar;
    private double scrollPos, scrollVal;

    public AbstractGuiItemProcessor(T tileEntity, AbstractContainerItemProcessor container, ResourceLocation texture) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);
        scrollPos = 0;
        scrollVal = 0;
    }

    @Override
    public void initGui() {
        super.initGui();
        tileEntity.playerIsTracking = true;
        sendChatMessage("Player now tracking" + tileEntity);
        queue = tileEntity.getPacketQueue();
        /*queue = new ItemPacketQueue();
        queue.add(new ItemStack(ModItems.redgem, 64)).getDisplayName();
        queue.add(new ItemStack(ModItems.demoitem, 64)).getDisplayName();
        queue.add(new ItemStack(Items.DIAMOND, 64)).getDisplayName();
        queue.add(new ItemStack(Items.GOLD_INGOT, 64)).getDisplayName();
        queue.add(new ItemStack(Items.GOLD_NUGGET, 64)).getDisplayName();
        queue.add(new ItemStack(Items.GOLDEN_APPLE, 64)).getDisplayName();
        queue.add(new ItemStack(Items.GHAST_TEAR, 64)).getDisplayName();
        queue.add(new ItemStack(Items.FEATHER, 64)).getDisplayName();
        queue.add(new ItemStack(Items.FERMENTED_SPIDER_EYE, 64)).getDisplayName();
        queue.add(new ItemStack(Items.ACACIA_BOAT, 64)).getDisplayName();
        queue.add(new ItemStack(Items.APPLE, 64)).getDisplayName();*/
        visual = new GuiList(queue, LIST_POS[0], LIST_POS[1], guiLeft, guiTop);
        bar = visual.getBar();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        tileEntity.playerIsTracking = false;
        sendChatMessage("Player no longer tracking " + tileEntity);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        //progress bar
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png"));
        double progress = tileEntity.getFractionOfProcessCompleted();
        drawTexturedModalRect(guiLeft + getProgressBarCoords()[0], guiTop + getProgressBarCoords()[1], PROGRESS_BAR_U, PROGRESS_BAR_V,
                ((int)(progress * PROGRESS_BAR_WIDTH)), PROGRESS_BAR_HEIGHT);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        queue = tileEntity.getPacketQueue();
        boolean flag = Mouse.isButtonDown(0);

        if (!wasClicking && flag && bar.isMouseOver())
            isScrolling = true;
        else if (!flag)
            isScrolling = false;

        wasClicking = flag;

        // list//tileEntity.getPacketQueue();
        /*
        sendDebugMessage(queue.toString());
        sendDebugMessage("empty queue? " + queue.isEmpty());

        if (!queue.isEmpty()) {
        }*/

        int scroll = Mouse.getEventDWheel();
        boolean up = scroll > 0;
        scrollVal = up ? Math.log(Math.abs(scroll) + 1) : -Math.log(Math.abs(scroll) + 1);
        scrollPos = (bar.getY() - 24) / 59d;

        if (queue.size() > 0) {
            visual.drawList(Minecraft.getMinecraft(), this, this.itemRender, scrollPos);
            if (queue.size() > 3)
                bar.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, isScrolling, scrollVal / (double) queue.size());
        }
    }

    protected abstract int[] getProgressBarCoords();
}
