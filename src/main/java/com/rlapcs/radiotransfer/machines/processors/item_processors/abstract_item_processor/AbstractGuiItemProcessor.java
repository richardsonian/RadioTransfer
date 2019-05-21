package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractGuiMaterialProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMaterialProcessor<T> {
    private static final int WIDTH = 188;
    private static final int HEIGHT = 197;

    protected Coordinate LIST_POS;
    protected Coordinate PROGRESS_BAR_POS;

    public static final int PROGRESS_BAR_U = 8;
    public static final int PROGRESS_BAR_V = 15;
    public static final int PROGRESS_BAR_WIDTH = 9;
    public static final int PROGRESS_BAR_HEIGHT = 6;

    private GuiList visual;
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

        visual = new GuiList(Minecraft.getMinecraft(), this, tileEntity.getHandler(), LIST_POS.x, LIST_POS.y, guiLeft, guiTop, tileEntity);
        bar = visual.getBar();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        //progress bar
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png"));
        double progress = tileEntity.getFractionOfProcessCompleted();
        drawTexturedModalRect(guiLeft + PROGRESS_BAR_POS.x, guiTop + PROGRESS_BAR_POS.y, PROGRESS_BAR_U, PROGRESS_BAR_V,
                ((int) (progress * PROGRESS_BAR_WIDTH)), PROGRESS_BAR_HEIGHT);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean flag = Mouse.isButtonDown(0);

        if (!wasClicking && flag && bar.isMouseOver())
            isScrolling = true;
        else if (!flag)
            isScrolling = false;

        wasClicking = flag;

        int scroll = Mouse.getEventDWheel();
        boolean up = scroll > 0;
        scrollVal = up ? Math.log(Math.abs(scroll) + 1) : -Math.log(Math.abs(scroll) + 1);
        scrollPos = (bar.getY() - 24) / 59d;

        //sendDebugMessage(tileEntity.isRegisteredInMultiblock() + " : " + tileEntity.getController());
        if(tileEntity.getHandler() != null) {
            visual.drawList(mouseX, mouseY, partialTicks, this.itemRender, scrollPos);
            if (tileEntity.getHandler().size() > 4)
                bar.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, isScrolling, scrollVal / (double) tileEntity.getHandler().size());
        }
    }
}
