package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.machines.controllers.rx_controller.BlockRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.BlockTxController;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder.BlockItemDecoder;
import com.rlapcs.radiotransfer.machines._deprecated.demo.BlockDemoBlock;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.BlockReceiver;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.BlockTransmitter;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.BlockItemEncoder;
import com.rlapcs.radiotransfer.machines.power_supply.BlockPowerSupply;
import com.rlapcs.radiotransfer.machines.radio.BlockRadio;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@ObjectHolder("radiotransfer")
public class ModBlocks {
    /*
        Add public static final item fields here: They will be auto-filled with the instance of the item object
        which was created in init() and added to the registry. If an item has a different variable name than its
        registry name, it needs an @ObjectHolder tag.
        Example:
        public static final ItemClass item_name = null;
     */

    //demo
    public static final BlockDemoBlock demoblock = null;
    public static final BlockTransmitter transmitter = null;
    public static final BlockReceiver receiver = null;

    //multiblock radio
    public static final BlockRadio radio = null;

    public static final BlockTxController tx_controller = null;
    public static final BlockRxController rx_controller = null;

    public static final BlockItemEncoder item_encoder = null;
    public static final BlockItemDecoder item_decoder = null;

    public static final BlockPowerSupply power_supply = null;

    /**
     * Returns a list of instances of blocks that are to be added to the registry.
     * @return The list.
     */
    public static List<Block> getInstancesForRegistry() {
        List<Block> blocks = new ArrayList<>();

        /* Add one instance of each block here */

        //demo
        blocks.add(new BlockDemoBlock());
        blocks.add(new BlockTransmitter());
        blocks.add(new BlockReceiver());

        //multiblock radio
        blocks.add(new BlockRadio());
        blocks.add(new BlockTxController());
        blocks.add(new BlockRxController());
        blocks.add(new BlockItemEncoder());
        blocks.add(new BlockItemDecoder());
        blocks.add(new BlockPowerSupply());
        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        return blocks;
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        /*
            Init the models for all the blocks' Items. Each of the public static final fields at the top must also
            have a line here. If it has its own class, call .initModel() on the field. If not,
            call .setCustomModelResourceLocation() on

            Example:

            demoitem.initModel();
            -or-
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(anon_block), 0,
                new ModelResourceLocation(anon_block.getRegistryName(), "inventory"));
         */

        //demo
        demoblock.initModel();
        transmitter.initModel();
        receiver.initModel();

        //multiblock radio
        radio.initModel();
        tx_controller.initModel();
        rx_controller.initModel();
        item_encoder.initModel();
        item_decoder.initModel();
        power_supply.initModel();
    }

    /**
     * Gets all the blocks in static fields at the top of the class which were filled by the registry.
     * Useful if you need to get a list of all the blocks registered.
     * @return A list of the blocks.
     */
    public static List<Block> getAllBlocks() {
        List<Block> blocks = new ArrayList<>();

        Field[] fields = ModBlocks.class.getDeclaredFields();
        for (Field f : fields) {
            if(Modifier.isStatic(f.getModifiers())) {
                Object fieldValue = null;
                try {
                    fieldValue = f.get(null);
                } catch (IllegalAccessException e) {
                    //Shouldn't happen
                    System.err.println("Error getting all blocks from static fields.");
                    e.printStackTrace();
                }
                if (fieldValue instanceof Block) {
                    blocks.add((Block) fieldValue);
                }
            }
        }
        return blocks;
    }
}