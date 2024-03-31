package pl.xyundy.squaredadditions.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pl.xyundy.squaredadditions.SquaredAdditions;

public class ModItems {

    public static final Item COPPER_SWORD = registerItem("copper_sword",
            new SwordItem(ModToolMaterial.COPPER_INGOT, 3, -2.4F, new FabricItemSettings()));
    public static final Item COPPER_PICKAXE = registerItem("copper_pickaxe",
            new PickaxeItem(ModToolMaterial.COPPER_INGOT, 1, -2.8F, new FabricItemSettings()));
    public static final Item COPPER_SHOVEL = registerItem("copper_shovel",
            new ShovelItem(ModToolMaterial.COPPER_INGOT, 1.5F, -3.0F, new FabricItemSettings()));
    public static final Item COPPER_AXE = registerItem("copper_axe",
            new AxeItem(ModToolMaterial.COPPER_INGOT, 7.0F, -3.2F, new FabricItemSettings()));
    public static final Item COPPER_HOE = registerItem("copper_hoe",
            new HoeItem(ModToolMaterial.COPPER_INGOT, -1, -2.0F, new FabricItemSettings()));

    public static final Item EMERALD_SWORD = registerItem("emerald_sword",
            new SwordItem(ModToolMaterial.EMERALD, 3, -2.4F, new FabricItemSettings()));
    public static final Item EMERALD_PICKAXE = registerItem("emerald_pickaxe",
            new PickaxeItem(ModToolMaterial.EMERALD, 1, -2.8F, new FabricItemSettings()));
    public static final Item EMERALD_SHOVEL = registerItem("emerald_shovel",
            new ShovelItem(ModToolMaterial.EMERALD, 1.5F, -3.0F, new FabricItemSettings()));
    public static final Item EMERALD_AXE = registerItem("emerald_axe",
            new AxeItem(ModToolMaterial.EMERALD, 6.0F, -3.1F, new FabricItemSettings()));
    public static final Item EMERALD_HOE = registerItem("emerald_hoe",
            new HoeItem(ModToolMaterial.EMERALD, -2, -1.0F, new FabricItemSettings()));



    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SquaredAdditions.MOD_ID, name), item);
    }


    public static void itemGroupCombat(FabricItemGroupEntries entries) {
        entries.add(COPPER_SWORD);
        entries.add(COPPER_PICKAXE);

        entries.add(EMERALD_SWORD);
        entries.add(EMERALD_PICKAXE);
    }
    public static void itemGroupTools(FabricItemGroupEntries entries) {
        entries.add(COPPER_PICKAXE);
        entries.add(COPPER_SHOVEL);
        entries.add(COPPER_AXE);
        entries.add(COPPER_HOE);

        entries.add(EMERALD_PICKAXE);
        entries.add(EMERALD_SHOVEL);
        entries.add(EMERALD_AXE);
        entries.add(EMERALD_HOE);
    }

    public static void registerModItems() {
        SquaredAdditions.LOGGER.info("Registering Mod Items for " + SquaredAdditions.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::itemGroupTools);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::itemGroupCombat);

        SquaredAdditions.LOGGER.info("Registering Mod Items for " + SquaredAdditions.MOD_ID + " finished!");

    }
}
