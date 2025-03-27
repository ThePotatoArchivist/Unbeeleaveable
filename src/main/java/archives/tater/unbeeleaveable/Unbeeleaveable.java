package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.mixin.PointOfInterestTypesInvoker;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BeehiveBlockEntity.BeeData;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class Unbeeleaveable implements ModInitializer {
	//TODO: find out what you call a hive with no beeexit
	private static final int deed = 0;

	public static final String MOD_ID = "unbeeleaveable";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Block EXITLESS_BEEHIVE = Registry.register(
			Registries.BLOCK,
			id("exitless_beehive"),
			new ExitlessBeehiveBlock(AbstractBlock.Settings.create()
					.mapColor(MapColor.OAK_TAN)
					.instrument(NoteBlockInstrument.BASS)
					.strength(0.6F)
					.sounds(BlockSoundGroup.WOOD)
					.burnable()
	));

	public static final Block BEE_BOMB = Registry.register(
			Registries.BLOCK,
			id("bee_bomb"),
			new BeeBombBlock(AbstractBlock.Settings.copy(Blocks.BEE_NEST))
	);

	public static final Item EXITLESS_BEEHIVE_ITEM = Registry.register(
			Registries.ITEM,
			id("exitless_beehive"),
			new BlockItem(EXITLESS_BEEHIVE, new Item.Settings().component(DataComponentTypes.BEES, List.of()))
	);

	public static final Item BEE_BOMB_ITEM = Registry.register(
			Registries.ITEM,
			id("bee_bomb"),
			new BlockItem(BEE_BOMB, new Item.Settings().component(DataComponentTypes.BEES, Collections.nCopies(3, BeeData.create(0))))
	);

	public static final BlockEntityType<ExitlessBeehiveBlockEntity> EXITLESS_BEEHIVE_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			id("exitless_beehive"),
			BlockEntityType.Builder.create(ExitlessBeehiveBlockEntity::new, EXITLESS_BEEHIVE).build()
	);

	public static final RegistryKey<PointOfInterestType> EXITLESS_BEEHIVE_POI =
			RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, id("exitless_beehive"));

	public static final BlockEntityType<BeeBombBlockEntity> BEE_BOMB_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			id("bee_bomb"),
			BlockEntityType.Builder.create(BeeBombBlockEntity::new, BEE_BOMB).build()
	);

	public static final EntityType<BeeBombEntity> BEE_BOMB_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			id("bee_bomb"),
			EntityType.Builder.<BeeBombEntity>create(BeeBombEntity::new, SpawnGroup.MISC)
					.makeFireImmune()
					.dimensions(0.98f, 0.98f)
					.maxTrackingRange(10)
					.trackingTickInterval(10)
					.build()
	);

	public static final TagKey<Item> BEE_BOMB_INGREDIENT = TagKey.of(RegistryKeys.ITEM, id("bee_bomb_ingredient"));

	public static final RecipeSerializer<BeeBombCraftingRecipe> BEE_BOMB_CRAFTING_RECIPE = Registry.register(
			Registries.RECIPE_SERIALIZER,
			id("bee_bomb"),
			new SpecialRecipeSerializer<>(BeeBombCraftingRecipe::new)
	);

	public static final TickCriterion NEAR_BEE_EXPLOSION = Criteria.register(id("near_bee_explosion").toString(), new TickCriterion());

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		PointOfInterestTypesInvoker.register(
				Registries.POINT_OF_INTEREST_TYPE,
				EXITLESS_BEEHIVE_POI,
				ImmutableSet.copyOf(EXITLESS_BEEHIVE.getStateManager().getStates()), 0, 1
		);
	}
}
