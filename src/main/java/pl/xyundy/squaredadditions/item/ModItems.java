package pl.xyundy.squaredadditions.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pl.xyundy.squaredadditions.SquaredAdditions;
import pl.xyundy.squaredadditions.block.ModBlocks;

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

    public static final Item ROSE_GOLD_INGOT = registerItem("rose_gold_ingot",
            new Item(new FabricItemSettings()));
    public static final Item ROSE_GOLD_SWORD = registerItem("rose_gold_sword",
            new SwordItem(ModToolMaterial.ROSE_GOLD_INGOT, 3, -2.4F, new FabricItemSettings()));
    public static final Item ROSE_GOLD_PICKAXE = registerItem("rose_gold_pickaxe",
            new PickaxeItem(ModToolMaterial.ROSE_GOLD_INGOT, 1, -2.8F, new FabricItemSettings()));
    public static final Item ROSE_GOLD_SHOVEL = registerItem("rose_gold_shovel",
            new ShovelItem(ModToolMaterial.ROSE_GOLD_INGOT, 1.5F, -3.0F, new FabricItemSettings()));
    public static final Item ROSE_GOLD_AXE = registerItem("rose_gold_axe",
            new AxeItem(ModToolMaterial.ROSE_GOLD_INGOT, 6.0F, -3.0F, new FabricItemSettings()));
    public static final Item ROSE_GOLD_HOE = registerItem("rose_gold_hoe",
            new HoeItem(ModToolMaterial.ROSE_GOLD_INGOT, -1, -3.0F, new FabricItemSettings()));

    public static final Item ROSE_GOLD_HELMET = registerItem("rose_gold_helmet",
            new ArmorItem(ModArmorMaterial.ROSE_GOLD, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item ROSE_GOLD_CHESTPLATE = registerItem("rose_gold_chestplate",
            new ArmorItem(ModArmorMaterial.ROSE_GOLD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item ROSE_GOLD_LEGGINGS = registerItem("rose_gold_leggings",
            new ArmorItem(ModArmorMaterial.ROSE_GOLD, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item ROSE_GOLD_BOOTS = registerItem("rose_gold_boots",
            new ArmorItem(ModArmorMaterial.ROSE_GOLD, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SquaredAdditions.MOD_ID, name), item);
    }


    public static void itemGroupCombat(FabricItemGroupEntries entries) {
        entries.add(COPPER_SWORD);
        entries.add(COPPER_AXE);

        entries.add(EMERALD_SWORD);
        entries.add(EMERALD_AXE);

        entries.add(ROSE_GOLD_SWORD);
        entries.add(ROSE_GOLD_AXE);

        entries.add(ROSE_GOLD_HELMET);
        entries.add(ROSE_GOLD_CHESTPLATE);
        entries.add(ROSE_GOLD_LEGGINGS);
        entries.add(ROSE_GOLD_BOOTS);
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

        entries.add(ROSE_GOLD_PICKAXE);
        entries.add(ROSE_GOLD_SHOVEL);
        entries.add(ROSE_GOLD_AXE);
        entries.add(ROSE_GOLD_HOE);
    }

    public static void itemGroupIngredient(FabricItemGroupEntries entries) {
        entries.add(ROSE_GOLD_INGOT);
    }

    public static void itemGroupBuildingBlocks(FabricItemGroupEntries entries) {
        entries.add(ModBlocks.ROSE_GOLD_BLOCK);
    }

    public static void registerModItems() {
        SquaredAdditions.LOGGER.info("Registering Mod Items for " + SquaredAdditions.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::itemGroupTools);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::itemGroupCombat);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::itemGroupIngredient);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(ModItems::itemGroupBuildingBlocks);

        SquaredAdditions.LOGGER.info("Registering Mod Items for " + SquaredAdditions.MOD_ID + " finished!");

    }
}
