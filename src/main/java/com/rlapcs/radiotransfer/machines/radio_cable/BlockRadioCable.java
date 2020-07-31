package com.rlapcs.radiotransfer.machines.radio_cable;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachine;
import com.rlapcs.radiotransfer.generic.blocks.IRadioCableConnectable;
import com.rlapcs.radiotransfer.registries.ModBlocks;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.xml.soap.Text;

public class BlockRadioCable extends Block {
    public static final String REG_NAME = "radio_cable";

    // Properties that indicate if there is the same block in a certain direction.
    public static final UnlistedPropertyBlockAvailable NORTH = new UnlistedPropertyBlockAvailable("north");
    public static final UnlistedPropertyBlockAvailable SOUTH = new UnlistedPropertyBlockAvailable("south");
    public static final UnlistedPropertyBlockAvailable WEST = new UnlistedPropertyBlockAvailable("west");
    public static final UnlistedPropertyBlockAvailable EAST = new UnlistedPropertyBlockAvailable("east");
    public static final UnlistedPropertyBlockAvailable UP = new UnlistedPropertyBlockAvailable("up");
    public static final UnlistedPropertyBlockAvailable DOWN = new UnlistedPropertyBlockAvailable("down");

    public BlockRadioCable() {
        super(Material.IRON);
        setRegistryName(REG_NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + REG_NAME);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(REG_NAME, "normal"));
        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return BakedModelRadioCable.BAKED_MODEL;
            }
        };
        ModelLoader.setCustomStateMapper(this, ignoreState);
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        // For our item model we want to use a normal json model. This has to be called in
        // ClientProxy.postInit (not preInit) so that's why it is a separate method.
        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(RadioTransfer.MODID, REG_NAME));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(REG_NAME, "normal"); // how tf do i make this not crash
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { NORTH, SOUTH, WEST, EAST, UP, DOWN };
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;

        boolean north = isValidBlock(world, pos.north());
        boolean south = isValidBlock(world, pos.south());
        boolean west = isValidBlock(world, pos.west());
        boolean east = isValidBlock(world, pos.east());
        boolean up = isValidBlock(world, pos.up());
        boolean down = isValidBlock(world, pos.down());

        return extendedBlockState
                .withProperty(NORTH, north)
                .withProperty(SOUTH, south)
                .withProperty(WEST, west)
                .withProperty(EAST, east)
                .withProperty(UP, up)
                .withProperty(DOWN, down);
    }

    private boolean isValidBlock(IBlockAccess world, BlockPos pos) {
        Block neighbor = world.getBlockState(pos).getBlock();
        return neighbor instanceof BlockRadioCable || neighbor instanceof AbstractBlockMachine; // maybe make it a lil more specific
    }
    private boolean isSameBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == ModBlocks.radio_cable;
    }
}