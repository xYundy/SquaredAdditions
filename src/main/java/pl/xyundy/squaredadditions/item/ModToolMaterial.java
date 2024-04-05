package pl.xyundy.squaredadditions.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    COPPER_INGOT(ToolMaterials.STONE.getMiningLevel(), ToolMaterials.WOOD.getDurability(), ToolMaterials.IRON.getMiningSpeedMultiplier(), ToolMaterials.IRON.getAttackDamage(), ToolMaterials.STONE.getEnchantability(), () -> Ingredient.ofItems(Items.COPPER_INGOT)),
    EMERALD(ToolMaterials.IRON.getMiningLevel(), ToolMaterials.DIAMOND.getDurability(), ToolMaterials.DIAMOND.getMiningSpeedMultiplier(), ToolMaterials.IRON.getAttackDamage(), ToolMaterials.DIAMOND.getEnchantability(), () -> Ingredient.ofItems(Items.EMERALD)),
    ROSE_GOLD_INGOT(ToolMaterials.GOLD.getMiningLevel(), ToolMaterials.GOLD.getDurability() + COPPER_INGOT.getDurability(), ToolMaterials.GOLD.getMiningSpeedMultiplier(), ToolMaterials.GOLD.getAttackDamage(), ToolMaterials.GOLD.getEnchantability(), () -> Ingredient.ofItems(ModItems.ROSE_GOLD_INGOT));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    ModToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage,
                            int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy<>(repairIngredient);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
