package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.ItemDecoderGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.ItemEncoderGuiList;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiItemDecoder extends AbstractGuiItemProcessor<TileItemDecoder> {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_decoder.png");

    public GuiItemDecoder(TileItemDecoder tileEntity, ContainerItemDecoder container) {
        super(tileEntity, container, background);

        LIST_POS = new CoordinateXY(8, 27);
        PROGRESS_BAR_POS = new CoordinateXY(86, 56);
    }

    @Override
    public void initGui() {
        super.initGui();

        visual = new ItemDecoderGuiList(Minecraft.getMinecraft(), this, tileEntity.getHandler(), LIST_POS.x, LIST_POS.y, guiLeft, guiTop, tileEntity);
    }
}