package archives.tater.unbeeleaveable;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class BeeBombCraftingRecipe extends SpecialCraftingRecipe {

    private static final String BEES_KEY = "Bees";

    public BeeBombCraftingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        var tnt = false;
        var hive = false;

        for (var i = 0; i < input.getSize(); i++) {
            var stack = input.getStackInSlot(i);
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
                        stack.contains(DataComponentTypes.BEES));
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack hive = null;

        for (var i = 0; i < input.getSize(); i++) {
            var stack = input.getStackInSlot(i);
            if (isHive(stack)) {
                hive = stack;
                break;
            }
        }

        if (hive == null) {
            return ItemStack.EMPTY;
        }

        var beeBomb = Unbeeleaveable.BEE_BOMB_ITEM.getDefaultStack();

        var bees = hive.get(DataComponentTypes.BEES);
        if (bees != null)
            beeBomb.set(DataComponentTypes.BEES, bees);

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
