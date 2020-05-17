package com.rlapcs.radiotransfer.machines.processors.material_processor;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractContainerProcessor;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGuiMaterialProcessor<T extends AbstractTileMaterialProcessor> extends AbstractGuiMachine<T> {
    public AbstractGuiMaterialProcessor(T tileEntity, AbstractContainerProcessor container) {
        super(tileEntity, container);
    }

    @Override
    public void initGui() {
        super.initGui();
        //sendChatMessage("Gui opened.");
        //sendChatMessage("Client registered? " + tileEntity.isRegisteredInMultiblock());
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        //sendChatMessage("Gui closed.");
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
    }
}
