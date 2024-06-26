package pl.xyundy.squaredadditions.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import pl.xyundy.squaredadditions.block.ModBlocks;
import pl.xyundy.squaredadditions.item.ModItems;
import net.minecraft.data.client.Models;
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ROSE_GOLD_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.COPPER_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.COPPER_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.COPPER_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.COPPER_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.COPPER_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.EMERALD_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.EMERALD_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.EMERALD_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.EMERALD_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.EMERALD_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSE_GOLD_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSE_GOLD_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSE_GOLD_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSE_GOLD_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ROSE_GOLD_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.ROSE_GOLD_INGOT, Models.GENERATED);

        itemModelGenerator.register(ModItems.ROSE_GOLD_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSE_GOLD_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSE_GOLD_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.ROSE_GOLD_BOOTS, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMERALD_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMERALD_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMERALD_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMERALD_BOOTS, Models.GENERATED);
    }
}