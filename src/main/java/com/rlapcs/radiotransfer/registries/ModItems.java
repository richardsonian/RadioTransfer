package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.items.ICustomItemModel;
import com.rlapcs.radiotransfer.items.ItemEncryptionCard;
import com.rlapcs.radiotransfer.items.ItemFilterCard;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@ObjectHolder("radiotransfer")
public class ModItems {
    /*
        Add public static final item fields here: They will be auto-filled with the instance of the item object
        which was created in init() and added to the registry. If an item has a different variable name than its
        registry name, it needs an @ObjectHolder tag.
        Example:
        public static final ItemClass item_name = null;
     */

    //upgrade cards
    public static final Item speed_upgrade = null;
    public static final Item stack_upgrade = null;
    public static final Item stack_downgrade = null; // Are we using this?
    public static final ItemEncryptionCard encryption_card = null;
    public static final ItemFilterCard filter_card = null;

    //crafting resources
    public static final Item unbaked_resistor = null;
    public static final Item baked_resistor = null;
    public static final Item unbaked_capacitor = null;
    public static final Item baked_capacitor = null;
    public static final Item vacuum_tube = null;
    public static final Item glass_fiber = null;
    public static final Item blank_circuit_board = null;
    public static final Item circuit_board = null;


    /**
     * Returns a list of instances of items that are to be added to the registry.
     * @return The list.
     */
    public static List<Item> getInstancesForRegistry() {
        List<Item> items = new ArrayList<>();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        //~~~~~~~~Add one instance of each item here~~~~~~~~//
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        //~~~~~~~~~~~~~~~~~Upgrade Cards~~~~~~~~~~~~~~~~~~~~//
        items.add(newBasicItem("speed_upgrade"));
        items.add(newBasicItem("stack_upgrade"));
        items.add(newBasicItem("stack_downgrade"));
        items.add(new ItemEncryptionCard());
        items.add(new ItemFilterCard());

        //~~~~~~~~~~~~~~~~Crafting resources~~~~~~~~~~~~~~~//
        items.add(newBasicItem("unbaked_resistor"));
        items.add(newBasicItem("baked_resistor"));
        items.add(newBasicItem("unbaked_capacitor"));
        items.add(newBasicItem("baked_capacitor"));
        items.add(newBasicItem("vacuum_tube"));
        items.add(newBasicItem("glass_fiber"));
        items.add(newBasicItem("blank_circuit_board"));
        items.add(newBasicItem("circuit_board"));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        //~~~~~~~~~~~~~END ITEM INSTANCES~~~~~~~~~~~~~~~~~~~//
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        return items;
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        /*
            Init the models for all the items. Each of the public static final fields at the top must also
            have a line here. If it has its own class, call .initModel() on the field. If not,
            call .setCustomModelResourceLocation()

            Example:

            demoitem.initModel();
            -or-
            ModelLoader.setCustomModelResourceLocation(anon_item, 0, new ModelResourceLocation(anon_item.getRegistryName(), "inventory"));
         */
        for (Item i : getAllItems()) {
            if(i instanceof ICustomItemModel) {
                ((ICustomItemModel) i).initModel();
            }
            else {
                ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
            }
        }

        //Add Smelting recipes
        //Is there a better place for this?
        GameRegistry.addSmelting(unbaked_resistor, new ItemStack(baked_resistor, 1), 1.5f);
        GameRegistry.addSmelting(unbaked_capacitor, new ItemStack(baked_capacitor, 1), 1.5f);
    }

    /**
     * Gets all the items in static fields at the top of the class which were filled by the registry.
     * Useful if you need to get a list of all the items registered.
     * @return A list of the items.
     */
    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        Field[] fields = ModItems.class.getDeclaredFields();
        for (Field f : fields) {
            if(Modifier.isStatic(f.getModifiers())) {
                Object fieldValue = null;
                try {
                    fieldValue = f.get(null);
                } catch (IllegalAccessException e) {
                    //Shouldn't happen
                    System.err.println("Error getting all items from static fields.");
                    e.printStackTrace();
                }
                if (fieldValue instanceof Item) {
                    items.add((Item) fieldValue);
                }
            }
        }
        return items;
    }

    /**
     * Creates a simple item that doesn't need its own class
     * @param name The registry name for the item, ex "demo_item"
     * @param tab The creative tab for the item
     * @return An instance of the item
     */
    private static Item newBasicItem(String name, CreativeTabs tab) {
        return new Item()
                .setRegistryName(name)
                .setUnlocalizedName(RadioTransfer.MODID + "." + name)
                .setCreativeTab(tab);
    }
    private static Item newBasicItem(String name) {
        return newBasicItem(name, CreativeTabs.MISC);
    }
}
