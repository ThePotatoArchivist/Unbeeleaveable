package archives.tater.unbeeleaveable;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BeeBombCraftingRecipe extends SpecialCraftingRecipe {

    private static final String BEES_KEY = "Bees";

    public BeeBombCraftingRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        var tnt = false;
        var hive = false;

        for (var i = 0; i < inventory.size(); i++) {
            var stack = inventory.getStack(i);
            if (stack.isOf(Items.TNT) && !tnt) {
                tnt = true;
            } else if (isHive(stack) && !hive) {
                hive = true;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }

        return tnt && hive;
    }

    private boolean isHive(ItemStack stack) {
        return !stack.isOf(Unbeeleaveable.BEE_BOMB_ITEM) &&
                (stack.isIn(Unbeeleaveable.BEE_BOMB_INGREDIENT) ||
                        (stack.getNbt() != null && stack.getNbt().getCompound("BlockEntityTag").contains(BEES_KEY)));
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack hive = null;

        for (var i = 0; i < inventory.size(); i++) {
            var stack = inventory.getStack(i);
            if (isHive(stack)) {
                hive = stack;
                break;
            }
        }

        if (hive == null) {
            return ItemStack.EMPTY;
        }

        var beeBomb = Unbeeleaveable.BEE_BOMB_ITEM.getDefaultStack();

        if (hive.getNbt() == null) return beeBomb;

        var bees = hive.getNbt().getCompound("BlockEntityTag").getList(BEES_KEY, NbtCompound.COMPOUND_TYPE);

        var newBees = new NbtList();
        for (var i = 0; i < bees.size(); i++) {
            newBees.add(bees.getCompound(i).getCompound("EntityData"));
        }

        var bombNbt = new NbtCompound();
        bombNbt.put(BEES_KEY, newBees);
        beeBomb.setSubNbt("BlockEntityTag", bombNbt);

        return beeBomb;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Unbeeleaveable.BEE_BOMB_CRAFTING_RECIPE;
    }
}
