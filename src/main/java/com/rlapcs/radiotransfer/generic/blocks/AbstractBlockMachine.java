package com.rlapcs.radiotransfer.generic.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;



public abstract class AbstractBlockMachine extends Block implements ITileEntityProvider {
    protected Class<? extends TileEntity> tileEntityClass;

    public AbstractBlockMachine(Material material, Class<? extends TileEntity> tileEntityClass) {
        super(material);
        this.tileEntityClass = tileEntityClass;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {return true;}

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return tileEntityClass.getConstructor().newInstance();
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
            System.err.println("Error creating new TileEntity of type " + tileEntityClass.getName() + " for block " + this);
            return null;
        }
    }

    //---- DROPS + HARVESTING -----//

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);

        TileEntity te = tileEntityClass.isInstance(world.getTileEntity(pos)) ? tileEntityClass.cast(world.getTileEntity(pos)) : null;

        if(te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            for(int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if(stack != ItemStack.EMPTY) {
                    drops.add(stack);
                }
            }
        }
    }

    // ~~~~~~ other optional-to-override methods that we might want to tweak: ~~~~~~~~~~~~~~~

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    //public void breakBlock(World worldIn, BlockPos pos, IBlockState state) { super.breakBlock(worldIn, pos, state); }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    //public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) { super.onBlockHarvested(worldIn, pos, state, player); }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    //public Item getItemDropped(IBlockState state, Random rand, int fortune) { return super.getItemDropped(state, rand, fortune); } //super method returns block.getItemFromBlock()

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }
}
