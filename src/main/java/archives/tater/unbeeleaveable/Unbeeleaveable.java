package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.mixin.PointOfInterestTypesInvoker;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbeeleaveable implements ModInitializer {
	//TODO: find out what you call a hive with no beeexit
	private static final int deed = 0;

	public static final String MOD_ID = "unbeeleaveable";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Block EXITLESS_BEEHIVE = Registry.register(
			Registries.BLOCK,
			new Identifier(MOD_ID, "exitless_beehive"),
			new ExitlessBeehiveBlock(FabricBlockSettings.create()
					.mapColor(MapColor.OAK_TAN)
					.instrument(Instrument.BASS)
					.strength(0.6F)
					.sounds(BlockSoundGroup.WOOD)
					.burnable()
	));

	public static final Item EXITLESS_BEEHIVE_ITEM = Registry.register(
			Registries.ITEM,
			new Identifier(MOD_ID, "exitless_beehive"),
			new BlockItem(EXITLESS_BEEHIVE, new FabricItemSettings())
	);

	public static final BlockEntityType<ExitlessBeehiveBlockEntity> EXITLESS_BEEHIVE_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier(MOD_ID, "exitless_beehive"),
			FabricBlockEntityTypeBuilder.create(ExitlessBeehiveBlockEntity::new, EXITLESS_BEEHIVE).build()
	);

	public static final RegistryKey<PointOfInterestType> EXITLESS_BEEHIVE_POI =
			RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(MOD_ID, "exitless_beehive"));


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
