package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractItemProcessorList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractGuiMaterialProcessor;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMaterialProcessor<T> {
    protected CoordinateXY LIST_POS;
    protected CoordinateXY PROGRESS_BAR_POS;
    protected AbstractItemProcessorList visual;

    public static final CoordinateUV PROGRESS_BAR_UV = new CoordinateUV(8, 15);
    public static final DimensionWidthHeight PROGRESS_BAR_DIMS = new DimensionWidthHeight(9, 6);

    public AbstractGuiItemProcessor(T tileEntity, AbstractContainerItemProcessor container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 197);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        //progress scrollBar
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png"));
        double progress = tileEntity.getFractionOfProcessCompleted();
        drawTexturedModalRect(guiLeft + PROGRESS_BAR_POS.x, guiTop + PROGRESS_BAR_POS.y, PROGRESS_BAR_UV.u, PROGRESS_BAR_UV.v,
                ((int) (progress * PROGRESS_BAR_DIMS.width)), PROGRESS_BAR_DIMS.height);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        visual.drawList(mouseX, mouseY, partialTicks, this.itemRender);
    }
}
