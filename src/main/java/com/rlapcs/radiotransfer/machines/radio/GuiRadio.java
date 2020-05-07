package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.antennas.basic_antenna.BlockBasicAntenna;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.BlockItemEncoder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiRadio extends GuiContainer {
    private ResourceLocation texture;
    private TextureManager textureManager;
    private TileRadio tileEntity;
    private int xSize = 188;
    private int ySize = 163;
    protected int guiLeft;
    protected int guiTop;

    public GuiRadio(TileRadio tileEntity) {
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
        this.tileEntity = tileEntity;
        //sendDebugMessage("construct");
    }

    public void initGui() {
        super.initGui();
        //sendDebugMessage("initgui");
        textureManager = mc.getTextureManager();
        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //sendDebugMessage("drawscreen");
        drawRect(0, 0, width, height, 0x70000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawText();
        renderMultiblockLayout();
    }

    private void renderMultiblockLayout() {
        ItemStack item = new ItemStack(new BlockBasicAntenna(), 1);
        render(item, 50, 50, itemRender.getItemModelWithOverrides(item, null, null));
        //itemRender.renderItem(new ItemStack(new BlockItemEncoder(), 1), ItemCameraTransforms.TransformType.GUI);
        //itemRender.renderItemAndEffectIntoGUI(item, 10, 10);
    }

    private void drawText() {

    }

    private void render(ItemStack stack, int x, int y, IBakedModel bakedmodel)
    {
        //System.out.println("model: " + bakedmodel);
        GlStateManager.pushMatrix();
        ResourceLocation texture = new ResourceLocation(RadioTransfer.MODID, "textures/blocks/basic_antenna.png");
        mc.getTextureManager().bindTexture(texture);
        mc.getTextureManager().getTexture(texture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.renderQuads(bufferbuilder, bakedmodel.getQuads(null, enumfacing, 0L), -1, stack);
        }

        this.renderQuads(bufferbuilder, bakedmodel.getQuads(null, null, 0L), -1, stack);
        tessellator.draw();
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
    {
        GlStateManager.rotate(45f, 45f, 45f, 45f);
        GlStateManager.translate((float) xPosition, (float) yPosition, 100.0F + this.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(16.0F, 16.0F, 16.0F);

        if (isGui3d)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
    }

    private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack)
    {
        boolean flag = color == -1 && !stack.isEmpty();
        int i = 0;

        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex())
            {
                k = mc.getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }
}
