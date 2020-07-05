package com.rlapcs.radiotransfer.machines.radio_cable;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {

    public static final ModelRadioCable RADIO_CABLE = new ModelRadioCable();

    // make expandable if you wanna
    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(RadioTransfer.MODID) && "radio_cable".equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return RADIO_CABLE;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}