package com.rlapcs.radiotransfer.generic.renderers;

import com.rlapcs.radiotransfer.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RendererEntitiy extends TileEntitySpecialRenderer {
    private static ItemStack stack = new ItemStack(ModItems.baked_resistor, 1, 0);
    private static EntityItem entItem = new EntityItem(Minecraft.getMinecraft().world, 0D, 0D, 0D, stack);

    public static void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f)
    {
    }
}
