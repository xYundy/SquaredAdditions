package pl.xyundy.squaredadditions.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import pl.xyundy.squaredadditions.SquaredAdditions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pl.xyundy.squaredadditions.block.entity.MixedSlabBlockEntity;

public class ModBlocks {
    public static final Block ROSE_GOLD_BLOCK = registerBlock("rose_gold_block",
            new Block(FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK)));

    public static final Block MIXED_SLAB_BLOCK = registerBlock("mixed_slab_block",
            new MixedSlabBlock(
                    Blocks.QUARTZ_SLAB.getDefaultState(),
                    Blocks.NETHER_BRICK_SLAB.getDefaultState(),
                    FabricBlockSettings.create())
    );

    public static final BlockEntityType<MixedSlabBlockEntity> MIXED_SLAB_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(SquaredAdditions.MOD_ID, "mixed_slab_block_entity"),
            FabricBlockEntityTypeBuilder.create(MixedSlabBlockEntity::new, MIXED_SLAB_BLOCK).build()
    );

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);

        return Registry.register(Registries.BLOCK, new Identifier(SquaredAdditions.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(SquaredAdditions.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        SquaredAdditions.LOGGER.info("Registering Mod Blocks for " + SquaredAdditions.MOD_ID);
        SquaredAdditions.LOGGER.info("Registering Mod Blocks for " + SquaredAdditions.MOD_ID + " finished!");
    }
}
