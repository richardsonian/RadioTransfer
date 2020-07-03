package com.rlapcs.radiotransfer.machines.radio_cable;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/*
 * Docs to Read:
 * https://mcforge.readthedocs.io/en/1.12.x/tileentities/tesr/
 * https://wiki.mcjty.eu/modding/index.php?title=Render_Block_TESR_/_OBJ-1.12
 */
public class RendererRadioCable extends TileEntitySpecialRenderer<TileRadioCable> { //could use TESRFast probably, but note that you have to make changes in other places to do that

    @Override
    public void render(TileRadioCable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        //~~~~~~~Render here, see McJty for example~~~~~~//



        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
