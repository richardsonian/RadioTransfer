package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.ItemEncoderGuiList;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiItemEncoder extends AbstractGuiItemProcessor<TileItemEncoder> {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_encoder.png");

    public GuiItemEncoder(TileItemEncoder tileEntity, ContainerItemEncoder container) {
        super(tileEntity, container, background);
        LIST_POS = new CoordinateXY(115, 27);
        PROGRESS_BAR_POS = new CoordinateXY(93, 55);
    }

    @Override
    public void initGui() {
        super.initGui();

        visual = new ItemEncoderGuiList(Minecraft.getMinecraft(), this, tileEntity.getHandler(), LIST_POS.x, LIST_POS.y, guiLeft, guiTop, tileEntity);
        bar = visual.getBar();
    }
}
